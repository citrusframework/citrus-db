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
import com.consol.citrus.db.server.rules.RuleMatcher;

import java.util.regex.Pattern;

@SuppressWarnings({"unused", "WeakerAccess"})
public class StatementRuleBuilder {

    private RuleBasedController controller;

    public StatementRuleBuilder(final RuleBasedController controller) {
        this.controller = controller;
    }

    public CreateStatementRuleBuilder create() {
        return new CreateStatementRuleBuilder(controller);
    }

    public CreatePreparedStatementRuleBuilder prepare() {
        return prepare(RuleMatcher.matchAll());
    }

    public CreatePreparedStatementRuleBuilder prepare(final String sql) {
        return prepare((stmt) -> stmt.equals(sql));
    }

    public CreatePreparedStatementRuleBuilder prepare(final RuleMatcher<String> matcher) {
        return new CreatePreparedStatementRuleBuilder(matcher, controller);
    }

    public ExecuteQueryRuleBuilder executeQuery(final String sql) {
        return new ExecuteQueryRuleBuilder((stmt) -> stmt.equals(sql), controller);
    }

    public ExecuteQueryRuleBuilder executeQuery(final Pattern sql) {
        return new ExecuteQueryRuleBuilder((stmt) -> sql.matcher(stmt).matches(), controller);
    }

    public ExecuteUpdateRuleBuilder executeUpdate(final String sql) {
        return new ExecuteUpdateRuleBuilder((stmt) -> stmt.equals(sql), controller);
    }

    public ExecuteUpdateRuleBuilder executeUpdate(final Pattern sql) {
        return new ExecuteUpdateRuleBuilder((stmt) -> sql.matcher(stmt).matches(), controller);
    }

    public CloseStatementRuleBuilder close() {
        return new CloseStatementRuleBuilder(controller);
    }
}