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

import com.consol.citrus.db.server.JdbcServerException;
import com.consol.citrus.db.server.controller.RuleBasedController;
import com.consol.citrus.db.server.rules.ExecuteUpdateRule;
import com.consol.citrus.db.server.rules.Mapping;
import com.consol.citrus.db.server.rules.Precondition;


@SuppressWarnings({"unused", "WeakerAccess"})
public class ExecuteUpdateRuleBuilder extends AbstractRuleBuilder<ExecuteUpdateRule, String, Integer>{

    private final Precondition<String> precondition;

    public ExecuteUpdateRuleBuilder(final Precondition<String> precondition, final RuleBasedController controller) {
        super(controller);
        this.precondition = precondition;
    }

    public ExecuteUpdateRule thenReturn(final Integer rowsUpdated) {
        return createRule(precondition, (any) -> rowsUpdated);
    }

    public ExecuteUpdateRule thenReturn() {
        return createRule(precondition, (any) -> 0);
    }

    public ExecuteUpdateRule thenThrow(final JdbcServerException e) {
        return createRule(precondition, (any) -> { throw e; });
    }

    @Override
    protected ExecuteUpdateRule createRule(final Precondition<String> matcher, final Mapping<String, Integer> executor) {
        final ExecuteUpdateRule executeUpdateRule = new ExecuteUpdateRule(matcher, executor);
        addRule(executeUpdateRule);
        return executeUpdateRule;
    }

    Precondition<String> getPrecondition() {
        return precondition;
    }
}