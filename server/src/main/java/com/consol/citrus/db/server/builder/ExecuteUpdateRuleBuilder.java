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
import com.consol.citrus.db.server.rules.Precondition;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ExecuteUpdateRuleBuilder {

    private final Precondition<String> precondition;
    private RuleBasedController controller;

    public ExecuteUpdateRuleBuilder(final Precondition<String> precondition, final RuleBasedController controller) {
        this.precondition = precondition;
        this.controller = controller;
    }

    public ExecuteUpdateRule thenReturn(final Integer rowsUpdated) {
        final ExecuteUpdateRule rule = new ExecuteUpdateRule(precondition, (any) -> rowsUpdated);
        controller.add(rule);
        return rule;
    }

    public ExecuteUpdateRule thenReturn() {
        final ExecuteUpdateRule rule = new ExecuteUpdateRule(precondition, (any) -> 0);
        controller.add(rule);
        return rule;
    }

    public ExecuteUpdateRule thenThrow(final JdbcServerException e) {
        final ExecuteUpdateRule rule = new ExecuteUpdateRule(precondition, (any) -> { throw e; });
        controller.add(rule);
        return rule;
    }

    RuleBasedController getController() {
        return controller;
    }

    Precondition<String> getPrecondition() {
        return precondition;
    }
}