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

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;

import java.lang.instrument.Instrumentation;
import java.sql.Driver;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @author Christoph Deppisch
 */
public class JdbcDriverAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        AgentBuilder agent = new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .type(isSubTypeOf(Driver.class))
                .transform((builder, type, classLoader, module) ->
                        builder.method(named("connect"))
                                .intercept(MethodDelegation.to(JdbcDriverInterceptor.class))
                );

        agent.installOn(inst);
    }
}
