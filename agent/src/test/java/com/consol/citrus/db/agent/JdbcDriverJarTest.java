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

import org.testng.Assert;
import org.testng.annotations.Test;

import static java.util.Arrays.asList;

public class JdbcDriverJarTest {

    @Test
    void testCmdlineParserWildfly() {
        String[] cmdlineArgs = new String[]{
                "-D[Standalone]",
                "-Xbootclasspath/p:/tmp/wildfly-10.1.0.Final/modules/system/layers/base/org/jboss/logmanager/main/jboss-logmanager-2.0.4.Final.jar",
                "-Djboss.modules.system.pkgs=org.jboss.logmanager",
                "-Djava.util.logging.manager=org.jboss.logmanager.LogManager",
                "-javaagent:../agent/target/citrus-db-agent.jar",
                "-Dorg.jboss.boot.log.file=/tmp/wildfly-10.1.0.Final/standalone/log/server.log",
                "-Dlogging.configuration=file:/tmp/wildfly-10.1.0.Final/standalone/configuration/logging.properties"
        };
        Assert.assertEquals("../agent/target/citrus-db-agent.jar", JdbcAgentJar.findAgentJarFromCmdline(asList(cmdlineArgs)).toString());
    }

    @Test
    void testCmdlineParserVersioned() {
        String[] cmdlineArgs = new String[] {
                "-javaagent:citrus-db-agent-2.0-SNAPSHOT.jar"
        };
        Assert.assertEquals("citrus-db-agent-2.0-SNAPSHOT.jar", JdbcAgentJar.findAgentJarFromCmdline(asList(cmdlineArgs)).toString());
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*citrus-db-agent\\.jar.*")
    void testCmdlineParserFailed() {
        String[] cmdlineArgs = new String[] {
                "-javaagent:/some/other/agent.jar",
                "-jar",
                "citrus-db-agent.jar"
        };
        JdbcAgentJar.findAgentJarFromCmdline(asList(cmdlineArgs));
    }
}
