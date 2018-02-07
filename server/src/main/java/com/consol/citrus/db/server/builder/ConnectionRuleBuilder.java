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
import com.consol.citrus.db.server.rules.Precondition;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ConnectionRuleBuilder {

    private RuleBasedController controller;

    public ConnectionRuleBuilder(final RuleBasedController controller){
        this.controller = controller;
    }

    public CloseConnectionRuleBuilder close() {
        return new CloseConnectionRuleBuilder(controller);
    }

    public OpenConnectionRuleBuilder open() {
        return open(Precondition.matchAll());
    }

    public OpenConnectionRuleBuilder open(final String username) {
        return open((properties) ->
                propertyMatchesValue(properties.get("username"), username));
    }

    public OpenConnectionRuleBuilder open(final String username, final String password) {
        return open((properties) ->
                propertyMatchesValue(properties.get("username"), username) &&
                        propertyMatchesValue(properties.get("password") ,password));
    }

    public OpenConnectionRuleBuilder open(final Precondition<Map<String, String>> precondition) {
        return new OpenConnectionRuleBuilder(precondition, controller);
    }

    public StartTransactionRuleBuilder startTransaction() {
        return new StartTransactionRuleBuilder(controller);
    }

    public CommitTransactionRuleBuilder commitTransaction() {
        return new CommitTransactionRuleBuilder(controller);
    }

    public RollbackTransactionRuleBuilder rollbackTransaction() {
        return new RollbackTransactionRuleBuilder(controller);
    }

    private boolean propertyMatchesValue(final String property, final String value) {
        return Optional.ofNullable(property)
                .orElse("")
                .equalsIgnoreCase(value);
    }

    RuleBasedController getController() {
        return controller;
    }
}
