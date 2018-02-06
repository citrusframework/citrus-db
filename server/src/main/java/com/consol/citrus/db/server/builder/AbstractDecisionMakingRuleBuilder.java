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
import com.consol.citrus.db.server.rules.Rule;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractDecisionMakingRuleBuilder<T extends Rule<P, Boolean, T>, P>
        extends AbstractRuleBuilder<T, P, Boolean>{

    public AbstractDecisionMakingRuleBuilder(final RuleBasedController controller) {
        super(controller);
    }

    public T thenAccept() {
        final T rule = createRule(Precondition.matchAll(), (any) -> true);
        addRule(rule);
        return rule;
    }

    public T thenDecline() {
        final T rule = createRule(Precondition.matchAll(), (any) -> false);
        addRule(rule);
        return rule;
    }
}
