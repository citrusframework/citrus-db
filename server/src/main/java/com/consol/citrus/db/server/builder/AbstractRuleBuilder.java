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

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractRuleBuilder<T extends Rule<P, Boolean, T>, P> {


    private RuleBasedController controller;

    public AbstractRuleBuilder(final RuleBasedController controller){
        this.controller = controller;
    }

    public T thenAccept() {
        final T rule = createRule(Precondition.matchAll(), (any) -> true);
        controller.add(rule);
        return rule;
    }

    public T thenDecline() {
        final T rule = createRule(Precondition.matchAll(), (any) -> false);
        controller.add(rule);
        return rule;
    }

    public T thenThrow(final JdbcServerException e) {
        final T rule = createRule(Precondition.matchAll(), (any) -> { throw e; });
        controller.add(rule);
        return rule;
    }

    protected abstract T createRule(Precondition<P> matcher, Mapping<P, Boolean> executor);

    RuleBasedController getController() {
        return controller;
    }
}
