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
import com.consol.citrus.db.server.rules.CreatePreparedStatementRule;
import com.consol.citrus.db.server.rules.Mapping;
import com.consol.citrus.db.server.rules.Precondition;

@SuppressWarnings({"unused", "WeakerAccess"})
public class CreatePreparedStatementRuleBuilder extends AbstractDecisionMakingRuleBuilder<CreatePreparedStatementRule, String> {

    public CreatePreparedStatementRuleBuilder(
            final Precondition<String> precondition,
            final RuleBasedController controller) {
        super(controller);
        setPrecondition(precondition);
    }

    @Override
    protected CreatePreparedStatementRule createRule(
            final Precondition<String> precondition,
            final Mapping<String, Boolean> mapping) {
        return new CreatePreparedStatementRule(precondition, mapping);
    }
}
