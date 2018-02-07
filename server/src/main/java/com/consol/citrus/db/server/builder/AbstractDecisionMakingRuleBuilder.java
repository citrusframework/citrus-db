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
import com.consol.citrus.db.server.rules.Mapping;
import com.consol.citrus.db.server.rules.Precondition;
import com.consol.citrus.db.server.rules.Rule;

/**
 * This class represents all rule builder that generate @{@link Rule} where the mapping result is a boolean value.
 * @param <T> {@inheritDoc}
 * @param <P> The input type of the rule to be mapped to the boolean value
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractDecisionMakingRuleBuilder<T extends Rule<P, Boolean, T>, P>
        extends AbstractRuleBuilder<T, P, Boolean>{

    /** The optional @{@link Precondition} to add to the @{@link Rule}*/
    private Precondition<P> precondition;

    /**
     * The constructor accepting a @{@link RuleBasedController}
     * @param controller The controller to add the rules to
     */
    public AbstractDecisionMakingRuleBuilder(final RuleBasedController controller) {
        super(controller);
    }

    /**
     * Creates a rule that accepts the previously described scenario
     * @return A accepting Rule
     */
    public T thenAccept() {
        if(precondition != null){
            return thenAcceptWithPrecondition(precondition);
        }else {
            return thenAcceptWithoutPrecondition();
        }
    }

    /**
     * Creates a rule that refuses the previously described scenario
     * @return A declining Rule
     */
    public T thenRefuse() {
        if(precondition != null){
            return thenRefuseWithPrecondition(precondition);
        }else {
            return thenRefuseWithoutPrecondition();
        }
    }

    /**
     * Create a rule that returns a acceptance result without precondition
     * @return A accepting Rule
     */
    private T thenAcceptWithoutPrecondition() {
        final T rule = createRule(Precondition.matchAll(), accept());
        addRule(rule);
        return rule;
    }

    /**
     * Create a rule that returns a acceptance result in case that the object,
     * the rule will be applied on, fulfills the precondition.
     * @return A accepting Rule
     */
    private T thenAcceptWithPrecondition(final Precondition<P> precondition) {
        final T rule = createRule(precondition, accept());
        addRule(rule);
        return rule;
    }

    /**
     * Create a rule that returns a refusing result without precondition
     * @return A accepting Rule
     */
    private T thenRefuseWithoutPrecondition() {
        final T rule = createRule(Precondition.matchAll(), refuse());
        addRule(rule);
        return rule;
    }

    /**
     * Create a rule that returns a refusing result in case that the object,
     * the rule will be applied on, fulfills the precondition.
     * @return A accepting Rule
     */
    private T thenRefuseWithPrecondition(final Precondition<P> precondition) {
        final T rule = createRule(precondition, refuse());
        addRule(rule);
        return rule;
    }

    /** Returns a @{@link Mapping} that accepts any value */
    private Mapping<P, Boolean> accept() {
        return (any) -> true;
    }

    /** Return a @{@link Mapping}, that refuses any value */
    private Mapping<P, Boolean> refuse() {
        return (any) -> false;
    }


    protected void setPrecondition(final Precondition<P> precondition) {
        this.precondition = precondition;
    }

    protected Precondition<P> getPrecondition() {
        return precondition;
    }
}
