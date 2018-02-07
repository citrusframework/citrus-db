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
import com.consol.citrus.db.server.rules.Mapping;
import com.consol.citrus.db.server.rules.Precondition;
import com.consol.citrus.db.server.rules.Rule;

/**
 * This class represents all rule builder generating @{@link Rule} objects.
 * @param <T> The type of the rule this builder is creating
 * @param <P> The input type of the rule to be mapped
 * @param <R> The resulting type after mapping
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractRuleBuilder<T extends Rule<P, R, T>, P, R> {

    /** The @{@link RuleBasedController} to manipulate */
    private RuleBasedController controller;

    /**
     * Constructor accepting a @{@link RuleBasedController}
     * @param controller The @{@link RuleBasedController} to manipulate
     */
    public AbstractRuleBuilder(final RuleBasedController controller){
        this.controller = controller;
    }

    /**
     * Returns a @{@link Rule} that throws a @{@link JdbcServerException}
     * @param exception The @{@link JdbcServerException} to be thrown
     * @return A @{@link Rule} throwing the exception
     */
    public T thenThrow(final JdbcServerException exception) {
        final T rule = createRule(Precondition.matchAll(), (any) -> { throw exception; });
        addRule(rule);
        return rule;
    }

    /**
     * Adds the given @{@link Rule} to the controller
     * @param rule The @{@link Rule} to be added
     */
    protected void addRule(final T rule){
        controller.add(rule);
    }

    /**
     * Creates a @{@link Rule} using the provided precondition and mapping
     * @param precondition The @{@link Precondition} to check before the @{@link Mapping} is applied.
     * @param mapping The @{@link Mapping} to apply to the provided object
     * @return A @{@link Rule} using the given parameter
     */
    protected abstract T createRule(Precondition<P> precondition, Mapping<P, R> mapping);

    RuleBasedController getController() {
        return controller;
    }
}
