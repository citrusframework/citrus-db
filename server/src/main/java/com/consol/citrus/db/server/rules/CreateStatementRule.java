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

/**
 * @author Christoph Deppisch
 */
public class CreateStatementRule extends Rule<Void, Boolean, CreateStatementRule> {

    public CreateStatementRule() {
        super();
    }

    public CreateStatementRule(Mapping<Void, Boolean> mapping) {
        super(mapping);
    }

    public CreateStatementRule(Precondition<Void> precondition, Mapping<Void, Boolean> mapping) {
        super(precondition, mapping);
    }
}
