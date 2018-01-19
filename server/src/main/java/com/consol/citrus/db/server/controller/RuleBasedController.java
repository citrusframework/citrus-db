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

package com.consol.citrus.db.server.controller;

import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.server.JdbcServerException;
import com.consol.citrus.db.server.rules.*;

import java.util.*;

/**
 * @author Christoph Deppisch
 */
public class RuleBasedController extends SimpleJdbcController {

    private List<OpenConnectionRule> openConnectionRules = new ArrayList<>();
    private List<CloseConnectionRule> closeConnectionRules = new ArrayList<>();
    private List<CreateStatementRule> createStatementRules = new ArrayList<>();
    private List<CreatePreparedStatementRule> createPreparedStatementRules = new ArrayList<>();
    private List<CloseStatementRule> closeStatementRules = new ArrayList<>();
    private List<ExecuteQueryRule> executeQueryRules = new ArrayList<>();
    private List<ExecuteUpdateRule> executeUpdateRules = new ArrayList<>();

    @Override
    protected DataSet handleQuery(String sql) throws JdbcServerException {
        return executeQueryRules.stream()
                .filter(rule -> rule.matches(sql))
                .findFirst()
                .orElse(new ExecuteQueryRule(super::handleQuery))
                .apply(sql);
    }

    @Override
    protected int handleUpdate(String sql) throws JdbcServerException {
        return executeUpdateRules.stream()
                .filter(rule -> rule.matches(sql))
                .findFirst()
                .orElse(new ExecuteUpdateRule(super::handleUpdate))
                .apply(sql);
    }

    @Override
    public void openConnection(Map<String, String> properties) throws JdbcServerException {
        openConnectionRules.stream()
                .filter(rule -> rule.matches(properties))
                .findFirst()
                .orElse(new OpenConnectionRule())
                .apply(properties);

        super.openConnection(properties);
    }

    @Override
    public void closeConnection() throws JdbcServerException {
        closeConnectionRules.stream()
                .filter(rule -> rule.matches(null))
                .findFirst()
                .orElse(new CloseConnectionRule())
                .apply(null);

        super.closeConnection();
    }

    @Override
    public void createStatement() throws JdbcServerException {
        createStatementRules.stream()
                .filter(rule -> rule.matches(null))
                .findFirst()
                .orElse(new CreateStatementRule())
                .apply(null);

        super.createStatement();
    }

    @Override
    public void createPreparedStatement(String sql) throws JdbcServerException {
        createPreparedStatementRules.stream()
                .filter(rule -> rule.matches(sql))
                .findFirst()
                .orElse(new CreatePreparedStatementRule())
                .apply(null);

        super.createPreparedStatement(sql);
    }

    @Override
    public void closeStatement() throws JdbcServerException {
        closeStatementRules.stream()
                .filter(rule -> rule.matches(null))
                .findFirst()
                .orElse(new CloseStatementRule())
                .apply(null);

        super.closeStatement();
    }

    RuleBasedController add(Rule rule) {
        if (rule instanceof OpenConnectionRule) {
            add((OpenConnectionRule) rule);
        } else if (rule instanceof CloseConnectionRule) {
            add((CloseConnectionRule) rule);
        } else if (rule instanceof CreateStatementRule) {
            add((CreateStatementRule) rule);
        } else if (rule instanceof CreatePreparedStatementRule) {
            add((CreatePreparedStatementRule) rule);
        } else if (rule instanceof CloseStatementRule) {
            add((CloseStatementRule) rule);
        } else if (rule instanceof ExecuteQueryRule) {
            add((ExecuteQueryRule) rule);
        } else if (rule instanceof ExecuteUpdateRule) {
            add((ExecuteUpdateRule) rule);
        }

        return this;
    }

    private void add(OpenConnectionRule rule) {
        this.openConnectionRules.add(rule);
    }

    private void add(CloseConnectionRule rule) {
        this.closeConnectionRules.add(rule);
    }

    private void add(CreateStatementRule rule) {
        this.createStatementRules.add(rule);
    }

    private void add(CreatePreparedStatementRule rule) {
        this.createPreparedStatementRules.add(rule);
    }

    private void add(CloseStatementRule rule) {
        this.closeStatementRules.add(rule);
    }

    private void add(ExecuteQueryRule rule) {
        this.executeQueryRules.add(rule);
    }

    private void add(ExecuteUpdateRule rule) {
        this.executeUpdateRules.add(rule);
    }
}
