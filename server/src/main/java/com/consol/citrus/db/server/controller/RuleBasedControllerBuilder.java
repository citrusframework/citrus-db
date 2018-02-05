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
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.TableDataSetProducer;
import com.consol.citrus.db.driver.json.JsonDataSetProducer;
import com.consol.citrus.db.driver.xml.XmlDataSetProducer;
import com.consol.citrus.db.server.JdbcServerException;
import com.consol.citrus.db.server.rules.CloseConnectionRule;
import com.consol.citrus.db.server.rules.CloseStatementRule;
import com.consol.citrus.db.server.rules.CreatePreparedStatementRule;
import com.consol.citrus.db.server.rules.CreateStatementRule;
import com.consol.citrus.db.server.rules.ExecuteQueryRule;
import com.consol.citrus.db.server.rules.ExecuteUpdateRule;
import com.consol.citrus.db.server.rules.OpenConnectionRule;
import com.consol.citrus.db.server.rules.Rule;
import com.consol.citrus.db.server.rules.RuleExecutor;
import com.consol.citrus.db.server.rules.RuleMatcher;

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

    public RuleBasedControllerBuilder(final RuleBasedController controller) {
        this.controller = controller;
    }

    public ConnectionRuleBuilder connection() {
        return new ConnectionRuleBuilder();
    }

    public StatementRuleBuilder statement() {
        return new StatementRuleBuilder();
    }

    @SuppressWarnings("unused")
    public class StatementRuleBuilder {

        public CreateStatementRuleBuilder create() {
            return new CreateStatementRuleBuilder();
        }

        public CreatePreparedStatementRuleBuilder prepare() {
            return prepare(RuleMatcher.matchAll());
        }

        public CreatePreparedStatementRuleBuilder prepare(final String sql) {
            return prepare((stmt) -> stmt.equals(sql));
        }

        public CreatePreparedStatementRuleBuilder prepare(final RuleMatcher<String> matcher) {
            return new CreatePreparedStatementRuleBuilder(matcher);
        }

        public ExecuteQueryRuleBuilder executeQuery(final String sql) {
            return new ExecuteQueryRuleBuilder((stmt) -> stmt.equals(sql));
        }

        public ExecuteQueryRuleBuilder executeQuery(final Pattern sql) {
            return new ExecuteQueryRuleBuilder((stmt) -> sql.matcher(stmt).matches());
        }

        public ExecuteUpdateRuleBuilder executeUpdate(final String sql) {
            return new ExecuteUpdateRuleBuilder((stmt) -> stmt.equals(sql));
        }

        public ExecuteUpdateRuleBuilder executeUpdate(final Pattern sql) {
            return new ExecuteUpdateRuleBuilder((stmt) -> sql.matcher(stmt).matches());
        }

        public CloseStatementRuleBuilder close() {
            return new CloseStatementRuleBuilder();
        }

        public class CreateStatementRuleBuilder extends AbstractRuleBuilder<CreateStatementRule, Void> {
            @Override
            protected CreateStatementRule createRule(final RuleMatcher<Void> matcher, final RuleExecutor<Void, Boolean> executor) {
                return new CreateStatementRule(matcher, executor);
            }
        }

        public class CreatePreparedStatementRuleBuilder extends AbstractRuleBuilder<CreatePreparedStatementRule, String> {
            private final RuleMatcher<String> ruleMatcher;

            private CreatePreparedStatementRuleBuilder(final RuleMatcher<String> ruleMatcher) {
                this.ruleMatcher = ruleMatcher;
            }

            @Override
            protected CreatePreparedStatementRule createRule(final RuleMatcher<String> matcher, final RuleExecutor<String, Boolean> executor) {
                return new CreatePreparedStatementRule(ruleMatcher, executor);
            }
        }

        public class ExecuteQueryRuleBuilder {
            private final RuleMatcher<String> ruleMatcher;

            private ExecuteQueryRuleBuilder(final RuleMatcher<String> ruleMatcher) {
                this.ruleMatcher = ruleMatcher;
            }

            public ExecuteQueryRule thenReturn(final DataSet dataSet) {
                final ExecuteQueryRule rule = new ExecuteQueryRule(ruleMatcher, (any) -> dataSet);
                controller.add(rule);
                return rule;
            }

            public ExecuteQueryRule thenReturn(final File file) {
                return thenReturn(file.toPath());
            }

            public ExecuteQueryRule thenReturn(final Path path) {
                final DataSet dataSet;

                try {
                    if (path.toString().endsWith(".json")) {
                        dataSet = new JsonDataSetProducer(path).produce();
                    } else if (path.toString().endsWith(".xml")) {
                        dataSet = new XmlDataSetProducer(path).produce();
                    } else {
                        dataSet = new TableDataSetProducer(new Table("empty")).produce();
                    }
                } catch (final SQLException e) {
                    throw new JdbcServerException(e);
                }

                final ExecuteQueryRule rule = new ExecuteQueryRule(ruleMatcher, (any) -> dataSet);
                controller.add(rule);
                return rule;
            }

            public ExecuteQueryRule thenThrow(final JdbcServerException e) {
                final ExecuteQueryRule rule = new ExecuteQueryRule(ruleMatcher, (any) -> { throw e; });
                controller.add(rule);
                return rule;
            }
        }

        public class ExecuteUpdateRuleBuilder {
            private final RuleMatcher<String> ruleMatcher;

            private ExecuteUpdateRuleBuilder(final RuleMatcher<String> ruleMatcher) {
                this.ruleMatcher = ruleMatcher;
            }

            public ExecuteUpdateRule thenReturn(final Integer rowsUpdated) {
                final ExecuteUpdateRule rule = new ExecuteUpdateRule(ruleMatcher, (any) -> rowsUpdated);
                controller.add(rule);
                return rule;
            }

            public ExecuteUpdateRule thenReturn() {
                final ExecuteUpdateRule rule = new ExecuteUpdateRule(ruleMatcher, (any) -> 0);
                controller.add(rule);
                return rule;
            }

            public ExecuteUpdateRule thenThrow(final JdbcServerException e) {
                final ExecuteUpdateRule rule = new ExecuteUpdateRule(ruleMatcher, (any) -> { throw e; });
                controller.add(rule);
                return rule;
            }
        }

        public class CloseStatementRuleBuilder extends AbstractRuleBuilder<CloseStatementRule, Void> {
            @Override
            protected CloseStatementRule createRule(final RuleMatcher<Void> matcher, final RuleExecutor<Void, Boolean> executor) {
                return new CloseStatementRule(matcher, executor);
            }
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public class ConnectionRuleBuilder {

        public CloseConnectionRuleBuilder close() {
            return new CloseConnectionRuleBuilder();
        }

        public OpenConnectionRuleBuilder open() {
            return open(RuleMatcher.matchAll());
        }

        public OpenConnectionRuleBuilder open(final String username) {
            return open((properties) ->
                    Optional.ofNullable(properties.get("username"))
                            .orElse("")
                            .equalsIgnoreCase(username));
        }

        public OpenConnectionRuleBuilder open(final String username, final String password) {
            return open((properties) ->
                   Optional.ofNullable(properties.get("username"))
                            .orElse("")
                            .equalsIgnoreCase(username) &&
                   Optional.ofNullable(properties.get("password"))
                            .orElse("")
                            .equalsIgnoreCase(password));
        }

        public OpenConnectionRuleBuilder open(final RuleMatcher<Map<String, String>> ruleMatcher) {
            return new OpenConnectionRuleBuilder(ruleMatcher);
        }

        public class OpenConnectionRuleBuilder extends AbstractRuleBuilder<OpenConnectionRule, Map<String, String>> {

            private final RuleMatcher<Map<String, String>> ruleMatcher;

            private OpenConnectionRuleBuilder(final RuleMatcher<Map<String, String>> ruleMatcher) {
                this.ruleMatcher = ruleMatcher;
            }

            @Override
            protected OpenConnectionRule createRule(final RuleMatcher<Map<String, String>> matcher, final RuleExecutor<Map<String, String>, Boolean> executor) {
                return new OpenConnectionRule(ruleMatcher, executor);
            }
        }

        public class CloseConnectionRuleBuilder extends AbstractRuleBuilder<CloseConnectionRule, Void> {
            @Override
            protected CloseConnectionRule createRule(final RuleMatcher<Void> matcher, final RuleExecutor<Void, Boolean> executor) {
                return new CloseConnectionRule(matcher, executor);
            }
        }
    }

    @SuppressWarnings("unused")
    public abstract class AbstractRuleBuilder<T extends Rule<P, Boolean, T>, P> {
        public T thenAccept() {
            final T rule = createRule(RuleMatcher.matchAll(), (any) -> true);
            controller.add(rule);
            return rule;
        }

        public T thenDecline() {
            final T rule = createRule(RuleMatcher.matchAll(), (any) -> false);
            controller.add(rule);
            return rule;
        }

        public T thenThrow(final JdbcServerException e) {
            final T rule = createRule(RuleMatcher.matchAll(), (any) -> { throw e; });
            controller.add(rule);
            return rule;
        }

        protected abstract T createRule(RuleMatcher<P> matcher, RuleExecutor<P, Boolean> executor);
    }

}
