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

import java.util.Map;

/**
 * @author Christoph Deppisch
 */
public class OpenConnectionRule extends Rule<Map<String, String>, Boolean, OpenConnectionRule> {

    public OpenConnectionRule() {
        super();
    }

    public OpenConnectionRule(Mapping<Map<String, String>, Boolean> mapping) {
        super(mapping);
    }

    public OpenConnectionRule(Precondition<Map<String, String>> precondition, Mapping<Map<String, String>, Boolean> mapping) {
        super(precondition, mapping);
    }
}
