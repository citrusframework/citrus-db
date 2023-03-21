/*
 * Copyright 2006-2017 the original author or authors.
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

package com.consol.citrus.jdbc.endpoint.builder;

import com.consol.citrus.endpoint.builder.AbstractEndpointBuilder;
import com.consol.citrus.jdbc.server.JdbcServerBuilder;

/**
 * Jdbc database server endpoint builder wrapper.
 *
 * @author Christoph Deppisch
 * @since 2.7.3
 */
public final class JdbcEndpoints extends AbstractEndpointBuilder<JdbcServerBuilder> {

    /**
     * Private constructor using server builder implementation.
     */
    private JdbcEndpoints() {
        super(new JdbcServerBuilder());
    }

    /**
     * Static entry method for Jdbc endpoints.
     * @return
     */
    public static JdbcEndpoints jdbc() {
        return new JdbcEndpoints();
    }

    /**
     * Returns browser builder for further fluent api calls.
     * @return
     */
    public JdbcServerBuilder server() {
        return builder;
    }
}
