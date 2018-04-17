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

import java.net.*;
import java.nio.file.Path;
import java.util.*;

public class ClassLoaderUtils {

    private static ClassLoaderUtils instance;

    private final JdbcAgentJar agentJar;

    /**
     * Prevent instantiation.
     * @param agentJar
     */
    private ClassLoaderUtils(JdbcAgentJar agentJar) {
        this.agentJar = agentJar;
    }

    public static synchronized ClassLoaderUtils getInstance() {
        if (instance == null) {
            instance = new ClassLoaderUtils(JdbcAgentJar.extract());
        }
        return instance;
    }

    public synchronized ClassLoader currentClassLoader() {
        return new URLClassLoader(pathsToURLs(agentJar.getJars()), Thread.currentThread().getContextClassLoader());
    }

    private static URL[] pathsToURLs(List<Path> paths) {
        try {
            URL[] result = new URL[paths.size()];
            for (int i=0; i<paths.size(); i++) {
                result[i] = paths.get(i).toUri().toURL();
            }
            return result;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
