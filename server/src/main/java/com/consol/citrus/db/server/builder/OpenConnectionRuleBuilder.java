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

package com.consol.citrus.db.server.builder;

import com.consol.citrus.db.server.controller.RuleBasedController;
import com.consol.citrus.db.server.rules.Mapping;
import com.consol.citrus.db.server.rules.OpenConnectionRule;
import com.consol.citrus.db.server.rules.Precondition;

import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class OpenConnectionRuleBuilder extends AbstractRuleBuilder<OpenConnectionRule, Map<String, String>> {

    private final Precondition<Map<String, String>> precondition;

    public OpenConnectionRuleBuilder(
            final Precondition<Map<String, String>> precondition,
            final RuleBasedController controller) {
        super(controller);
        this.precondition = precondition;
    }

    @Override
    protected OpenConnectionRule createRule(
            final Precondition<Map<String, String>> matcher,
            final Mapping<Map<String, String>, Boolean> executor) {
        return new OpenConnectionRule(precondition, executor);
    }

    public Precondition<Map<String, String>> getPrecondition() {
        return precondition;
    }
}