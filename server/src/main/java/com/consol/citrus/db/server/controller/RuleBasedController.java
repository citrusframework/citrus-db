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
import com.consol.citrus.db.server.rules.CloseConnectionRule;
import com.consol.citrus.db.server.rules.CloseStatementRule;
import com.consol.citrus.db.server.rules.CommitTransactionRule;
import com.consol.citrus.db.server.rules.CreatePreparedStatementRule;
import com.consol.citrus.db.server.rules.CreateStatementRule;
import com.consol.citrus.db.server.rules.ExecuteQueryRule;
import com.consol.citrus.db.server.rules.ExecuteUpdateRule;
import com.consol.citrus.db.server.rules.OpenConnectionRule;
import com.consol.citrus.db.server.rules.RollbackTransactionRule;
import com.consol.citrus.db.server.rules.Rule;
import com.consol.citrus.db.server.rules.StartTransactionRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Christoph Deppisch
 */
public class RuleBasedController extends AbstractJdbcController{

    private final AbstractJdbcController delegateJdbcController;

    private List<OpenConnectionRule> openConnectionRules = new ArrayList<>();
    private List<CloseConnectionRule> closeConnectionRules = new ArrayList<>();
    private List<CreateStatementRule> createStatementRules = new ArrayList<>();
    private List<CreatePreparedStatementRule> createPreparedStatementRules = new ArrayList<>();
    private List<CloseStatementRule> closeStatementRules = new ArrayList<>();
    private List<ExecuteQueryRule> executeQueryRules = new ArrayList<>();
    private List<ExecuteUpdateRule> executeUpdateRules = new ArrayList<>();
    private List<StartTransactionRule> startTransactionRule = new ArrayList<>();
    private List<CommitTransactionRule> commitTransactionRule = new ArrayList<>();
    private List<RollbackTransactionRule> rollbackTransactionRule = new ArrayList<>();

    public RuleBasedController() {
        delegateJdbcController = new SimpleJdbcController();
    }

    RuleBasedController(final AbstractJdbcController delegateJdbcController) {
        this.delegateJdbcController = delegateJdbcController;
    }


    @Override
    protected DataSet handleQuery(final String sql) throws JdbcServerException {
        return executeQueryRules.stream()
                .filter(rule -> rule.matches(sql))
                .findFirst()
                .orElse(new ExecuteQueryRule(delegateJdbcController::handleQuery))
                .applyOn(sql);
    }

    @Override
    protected int handleUpdate(final String sql) throws JdbcServerException {
        return executeUpdateRules.stream()
                .filter(rule -> rule.matches(sql))
                .findFirst()
                .orElse(new ExecuteUpdateRule(delegateJdbcController::handleUpdate))
                .applyOn(sql);
    }

    @Override
    public void openConnection(final Map<String, String> properties) throws JdbcServerException {
        openConnectionRules.stream()
                .filter(rule -> rule.matches(properties))
                .findFirst()
                .orElse(new OpenConnectionRule())
                .applyOn(properties);

        delegateJdbcController.openConnection(properties);
    }

    @Override
    public void closeConnection() throws JdbcServerException {
        closeConnectionRules.stream()
                .filter(rule -> rule.matches(null))
                .findFirst()
                .orElse(new CloseConnectionRule())
                .applyOn(null);

        delegateJdbcController.closeConnection();
    }

    @Override
    public void createStatement() throws JdbcServerException {
        createStatementRules.stream()
                .filter(rule -> rule.matches(null))
                .findFirst()
                .orElse(new CreateStatementRule())
                .applyOn(null);

        delegateJdbcController.createStatement();
    }

    @Override
    public void createPreparedStatement(final String sql) throws JdbcServerException {
        createPreparedStatementRules.stream()
                .filter(rule -> rule.matches(sql))
                .findFirst()
                .orElse(new CreatePreparedStatementRule())
                .applyOn(null);

        delegateJdbcController.createPreparedStatement(sql);
    }

    @Override
    public void closeStatement() throws JdbcServerException {
        closeStatementRules.stream()
                .filter(rule -> rule.matches(null))
                .findFirst()
                .orElse(new CloseStatementRule())
                .applyOn(null);

        delegateJdbcController.closeStatement();
    }

    @Override
    public void setTransactionState(final boolean transactionState) {
        if(transactionState){
            startTransactionRule.stream()
                    .filter(rule -> rule.matches(null))
                    .findFirst()
                    .orElse(new StartTransactionRule())
                    .applyOn(null);
        }

        delegateJdbcController.setTransactionState(transactionState);
    }

    @Override
    public void commitStatements() {
        commitTransactionRule.stream()
                .filter(rule -> rule.matches(null))
                .findFirst()
                .orElse(new CommitTransactionRule())
                .applyOn(null);

        delegateJdbcController.commitStatements();
    }

    @Override
    public void rollbackStatements() {
        rollbackTransactionRule.stream()
                .filter(rule -> rule.matches(null))
                .findFirst()
                .orElse(new RollbackTransactionRule())
                .applyOn(null);

        delegateJdbcController.rollbackStatements();
    }

    public RuleBasedController add(final Rule rule) {
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
        } else if (rule instanceof StartTransactionRule) {
            add((StartTransactionRule) rule);
        } else if (rule instanceof CommitTransactionRule) {
            add((CommitTransactionRule) rule);
        } else if (rule instanceof RollbackTransactionRule) {
            add((RollbackTransactionRule) rule);
        }

        return this;
    }

    private void add(final OpenConnectionRule rule) {
        this.openConnectionRules.add(rule);
    }

    private void add(final CloseConnectionRule rule) {
        this.closeConnectionRules.add(rule);
    }

    private void add(final CreateStatementRule rule) {
        this.createStatementRules.add(rule);
    }

    private void add(final CreatePreparedStatementRule rule) {
        this.createPreparedStatementRules.add(rule);
    }

    private void add(final CloseStatementRule rule) {
        this.closeStatementRules.add(rule);
    }

    private void add(final ExecuteQueryRule rule) {
        this.executeQueryRules.add(rule);
    }

    private void add(final ExecuteUpdateRule rule) {
        this.executeUpdateRules.add(rule);
    }

    private void add(final StartTransactionRule rule) {
        this.startTransactionRule.add(rule);
    }

    private void add(final CommitTransactionRule rule) {
        this.commitTransactionRule.add(rule);
    }

    private void add(final RollbackTransactionRule rule) {
        this.rollbackTransactionRule.add(rule);
    }

}
