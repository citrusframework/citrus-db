/*
 * Copyright 2006-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.db.agent;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.security.CodeSource;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The citrus-db-agent.jar contains all driver classes and their dependencies.
 * This class provides URLs to these JAR files.
 */
class JdbcAgentJar {

    private final List<Path> jars; // classes shared across multiple deployments

    private JdbcAgentJar(List<Path> jars) {
        this.jars = Collections.unmodifiableList(jars);
    }

    List<Path> getJars() {
        return jars;
    }

    /**
     * Theoretically we could return a list of jar:/ URLs without extracting the JARs,
     * but the URLClassLoader has a bug such that jar:/ URLs cannot be used. Therefore, we have
     * to extract the JARs and return a list of file:/ URLs.
     * See https://bugs.openjdk.java.net/browse/JDK-4735639
     */
    static JdbcAgentJar extract() {
        Path driverJar = findAgentJar();
        List<Path> extractedJars;
        try {
            Path tmpDir = Files.createTempDirectory("citrus-db-agent-");
            tmpDir.toFile().deleteOnExit();
            extractedJars = unzip(driverJar, tmpDir, entry -> entry.getName().endsWith(".jar"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load citrus-db-agent.jar: " + e.getMessage(), e);
        }
        List<Path> sharedJars = new ArrayList<>(extractedJars);

        return new JdbcAgentJar(sharedJars);
    }

    private static List<Path> unzip(Path jarFile, Path destDir, Predicate<JarEntry> filter) throws IOException {
        List<Path> result = new ArrayList<>();
        try (JarFile agentJar = new JarFile(jarFile.toFile())) {
            Enumeration<JarEntry> jarEntries = agentJar.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                if (filter.test(jarEntry)) {
                    Path destFile = destDir.resolve(jarEntry.getName());
                    if (!destFile.getParent().toFile().exists()) {
                        if (!destFile.getParent().toFile().mkdirs()) {
                            throw new IOException("Failed to make directory: " + destFile.getParent());
                        }
                    }
                    Files.copy(agentJar.getInputStream(jarEntry), destFile);
                    result.add(destFile);
                }
            }
        }
        return result;
    }

    private static Path findAgentJar() {
        CodeSource cs = JdbcDriverAgent.class.getProtectionDomain().getCodeSource();
        if (cs != null) {
            return findAgentJarFromCodeSource(cs);
        } else {
            // This happens if the Promagent class is loaded from the bootstrap class loader,
            // i.e. in addition to the command line argument -javaagent:/path/to/promagent.jar,
            // the argument -Xbootclasspath/p:/path/to/promagent.jar is used.
            return findAgentJarFromCmdline(ManagementFactory.getRuntimeMXBean().getInputArguments());
        }
    }

    private static Path findAgentJarFromCodeSource(CodeSource cs) {
        try {
            return Paths.get(cs.getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to load citrus-db-agent.jar from " + cs.getLocation() + ": " + e.getMessage(), e);
        }
    }

    static Path findAgentJarFromCmdline(List<String> cmdlineArgs) {
        Pattern p = Pattern.compile("^-javaagent:(.*citrus-db-agent([^/]*).jar)(=.*)?$");
        for (String arg : cmdlineArgs) {
            Matcher m = p.matcher(arg);
            if (m.matches()) {
                return Paths.get(m.group(1));
            }
        }
        throw new RuntimeException("Failed to locate citrus-db-agent.jar file.");
    }
}
