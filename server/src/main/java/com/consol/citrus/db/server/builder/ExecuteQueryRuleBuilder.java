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

import com.consol.citrus.db.driver.data.Table;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.TableDataSetProducer;
import com.consol.citrus.db.driver.json.JsonDataSetProducer;
import com.consol.citrus.db.driver.xml.XmlDataSetProducer;
import com.consol.citrus.db.server.JdbcServerException;
import com.consol.citrus.db.server.controller.RuleBasedController;
import com.consol.citrus.db.server.rules.ExecuteQueryRule;
import com.consol.citrus.db.server.rules.RuleMatcher;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ExecuteQueryRuleBuilder {

    private final RuleMatcher<String> ruleMatcher;
    private RuleBasedController controller;

    public ExecuteQueryRuleBuilder(final RuleMatcher<String> ruleMatcher, final RuleBasedController controller) {
        this.controller = controller;
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

    RuleBasedController getController() {
        return controller;
    }

    RuleMatcher<String> getRuleMatcher() {
        return ruleMatcher;
    }
}
