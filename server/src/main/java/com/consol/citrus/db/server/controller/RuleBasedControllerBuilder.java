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

import com.consol.citrus.db.driver.data.Table;
import com.consol.citrus.db.driver.dataset.*;
import com.consol.citrus.db.driver.json.JsonDataSetProducer;
import com.consol.citrus.db.driver.xml.XmlDataSetProducer;
import com.consol.citrus.db.server.JdbcServerException;
import com.consol.citrus.db.server.rules.*;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Christoph Deppisch
 */
public class RuleBasedControllerBuilder {

    private final RuleBasedController controller;

    public RuleBasedControllerBuilder(RuleBasedController controller) {
        this.controller = controller;
    }

    public ConnectionRuleBuilder connection() {
        return new ConnectionRuleBuilder();
    }

    public StatementRuleBuilder statement() {
        return new StatementRuleBuilder();
    }

    public class StatementRuleBuilder {

        public CreateStatementRuleBuilder create() {
            return new CreateStatementRuleBuilder();
        }

        public CreatePreparedStatementRuleBuilder prepare() {
            return prepare(RuleMatcher.matchAll());
        }

        public CreatePreparedStatementRuleBuilder prepare(String sql) {
            return prepare((stmt) -> stmt.equals(sql));
        }

        public CreatePreparedStatementRuleBuilder prepare(RuleMatcher<String> matcher) {
            return new CreatePreparedStatementRuleBuilder(matcher);
        }

        public ExecuteQueryRuleBuilder executeQuery(String sql) {
            return new ExecuteQueryRuleBuilder((stmt) -> stmt.equals(sql));
        }

        public ExecuteQueryRuleBuilder executeQuery(Pattern sql) {
            return new ExecuteQueryRuleBuilder((stmt) -> sql.matcher(stmt).matches());
        }

        public ExecuteUpdateRuleBuilder executeUpdate(String sql) {
            return new ExecuteUpdateRuleBuilder((stmt) -> stmt.equals(sql));
        }

        public ExecuteUpdateRuleBuilder executeUpdate(Pattern sql) {
            return new ExecuteUpdateRuleBuilder((stmt) -> sql.matcher(stmt).matches());
        }

        public CloseStatementRuleBuilder close() {
            return new CloseStatementRuleBuilder();
        }

        public class CreateStatementRuleBuilder extends AbstractRuleBuilder<CreateStatementRule, Void> {
            @Override
            protected CreateStatementRule createRule(RuleMatcher<Void> matcher, RuleExecutor<Void, Boolean> executor) {
                return new CreateStatementRule(matcher, executor);
            }
        }

        public class CreatePreparedStatementRuleBuilder extends AbstractRuleBuilder<CreatePreparedStatementRule, String> {
            private final RuleMatcher<String> ruleMatcher;

            private CreatePreparedStatementRuleBuilder(RuleMatcher<String> ruleMatcher) {
                this.ruleMatcher = ruleMatcher;
            }

            @Override
            protected CreatePreparedStatementRule createRule(RuleMatcher<String> matcher, RuleExecutor<String, Boolean> executor) {
                return new CreatePreparedStatementRule(ruleMatcher, executor);
            }
        }

        public class ExecuteQueryRuleBuilder {
            private final RuleMatcher<String> ruleMatcher;

            private ExecuteQueryRuleBuilder(RuleMatcher<String> ruleMatcher) {
                this.ruleMatcher = ruleMatcher;
            }

            public ExecuteQueryRule thenReturn(DataSet dataSet) {
                ExecuteQueryRule rule = new ExecuteQueryRule(ruleMatcher, (any) -> dataSet);
                controller.add(rule);
                return rule;
            }

            public ExecuteQueryRule thenReturn(File file) {
                return thenReturn(file.toPath());
            }

            public ExecuteQueryRule thenReturn(Path path) {
                DataSet dataSet;

                try {
                    if (path.toString().endsWith(".json")) {
                        dataSet = new JsonDataSetProducer(path).produce();
                    } else if (path.toString().endsWith(".xml")) {
                        dataSet = new XmlDataSetProducer(path).produce();
                    } else {
                        dataSet = new TableDataSetProducer(new Table("empty")).produce();
                    }
                } catch (SQLException e) {
                    throw new JdbcServerException(e);
                }

                ExecuteQueryRule rule = new ExecuteQueryRule(ruleMatcher, (any) -> dataSet);
                controller.add(rule);
                return rule;
            }

            public ExecuteQueryRule thenThrow(JdbcServerException e) {
                ExecuteQueryRule rule = new ExecuteQueryRule(ruleMatcher, (any) -> { throw e; });
                controller.add(rule);
                return rule;
            }
        }

        public class ExecuteUpdateRuleBuilder {
            private final RuleMatcher<String> ruleMatcher;

            private ExecuteUpdateRuleBuilder(RuleMatcher<String> ruleMatcher) {
                this.ruleMatcher = ruleMatcher;
            }

            public ExecuteUpdateRule thenReturn(Integer rowsUpdated) {
                ExecuteUpdateRule rule = new ExecuteUpdateRule(ruleMatcher, (any) -> rowsUpdated);
                controller.add(rule);
                return rule;
            }

            public ExecuteUpdateRule thenReturn() {
                ExecuteUpdateRule rule = new ExecuteUpdateRule(ruleMatcher, (any) -> 0);
                controller.add(rule);
                return rule;
            }

            public ExecuteUpdateRule thenThrow(JdbcServerException e) {
                ExecuteUpdateRule rule = new ExecuteUpdateRule(ruleMatcher, (any) -> { throw e; });
                controller.add(rule);
                return rule;
            }
        }

        public class CloseStatementRuleBuilder extends AbstractRuleBuilder<CloseStatementRule, Void> {
            @Override
            protected CloseStatementRule createRule(RuleMatcher<Void> matcher, RuleExecutor<Void, Boolean> executor) {
                return new CloseStatementRule(matcher, executor);
            }
        }
    }

    public class ConnectionRuleBuilder {

        public CloseConnectionRuleBuilder close() {
            return new CloseConnectionRuleBuilder();
        }

        public OpenConnectionRuleBuilder open() {
            return open(RuleMatcher.matchAll());
        }

        public OpenConnectionRuleBuilder open(String username) {
            return open((properties) ->
                    Optional.ofNullable(properties.get("username"))
                            .orElse("")
                            .equalsIgnoreCase(username));
        }

        public OpenConnectionRuleBuilder open(String username, String password) {
            return open((properties) ->
                   Optional.ofNullable(properties.get("username"))
                            .orElse("")
                            .equalsIgnoreCase(username) &&
                   Optional.ofNullable(properties.get("password"))
                            .orElse("")
                            .equalsIgnoreCase(password));
        }

        public OpenConnectionRuleBuilder open(RuleMatcher<Map<String, String>> ruleMatcher) {
            return new OpenConnectionRuleBuilder(ruleMatcher);
        }

        public class OpenConnectionRuleBuilder extends AbstractRuleBuilder<OpenConnectionRule, Map<String, String>> {

            private final RuleMatcher<Map<String, String>> ruleMatcher;

            private OpenConnectionRuleBuilder(RuleMatcher<Map<String, String>> ruleMatcher) {
                this.ruleMatcher = ruleMatcher;
            }

            @Override
            protected OpenConnectionRule createRule(RuleMatcher<Map<String, String>> matcher, RuleExecutor<Map<String, String>, Boolean> executor) {
                return new OpenConnectionRule(ruleMatcher, executor);
            }
        }

        public class CloseConnectionRuleBuilder extends AbstractRuleBuilder<CloseConnectionRule, Void> {
            @Override
            protected CloseConnectionRule createRule(RuleMatcher<Void> matcher, RuleExecutor<Void, Boolean> executor) {
                return new CloseConnectionRule(matcher, executor);
            }
        }
    }

    public abstract class AbstractRuleBuilder<T extends Rule<P, Boolean, T>, P> {
        public T thenAccept() {
            T rule = createRule(RuleMatcher.matchAll(), (any) -> true);
            controller.add(rule);
            return rule;
        }

        public T thenDecline() {
            T rule = createRule(RuleMatcher.matchAll(), (any) -> false);
            controller.add(rule);
            return rule;
        }

        public T thenThrow(JdbcServerException e) {
            T rule = createRule(RuleMatcher.matchAll(), (any) -> { throw e; });
            controller.add(rule);
            return rule;
        }

        protected abstract T createRule(RuleMatcher<P> matcher, RuleExecutor<P, Boolean> executor);
    }

}
