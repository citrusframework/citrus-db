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

package com.consol.citrus.db.server.rules;

import com.consol.citrus.db.server.JdbcServerException;

/**
 * This interface describes the transformation of an object of type P
 * into a object of type R by executing transformation rules
 *
 * @author Christoph Deppisch
 */
public interface RuleExecutor<P, R> {

    /**
     * Transforms an object of the domain type P into an object
     * of the codomain R by executing the underlying implementation.
     *
     * @param domainElement The domain element to be transformed
     * @return The transformed element of the codomain
     * @throws JdbcServerException In case of an transformation error
     */
    R then(P domainElement) throws JdbcServerException;
}
