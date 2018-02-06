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
import com.consol.citrus.db.server.rules.CreatePreparedStatementRule;
import com.consol.citrus.db.server.rules.RuleExecutor;
import com.consol.citrus.db.server.rules.RuleMatcher;
import org.testng.annotations.Test;

import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

public class CreatePreparedStatementRuleBuilderTest {

    private final RuleMatcher<String> ruleMatcher = RuleMatcher.matchAll();
    private RuleBasedController ruleBasedControllerMock = mock(RuleBasedController.class);
    private CreatePreparedStatementRuleBuilder createStatementRuleBuilder =
            new CreatePreparedStatementRuleBuilder(ruleMatcher, ruleBasedControllerMock);

    @Test
    public void testCreateRule(){

        //GIVEN
        final boolean expectedValue = new Random().nextBoolean();
        final RuleExecutor<String, Boolean> executor = (anything) -> expectedValue;

        //WHEN
        final CreatePreparedStatementRule createPreparedStatementRule =
                createStatementRuleBuilder.createRule(executor);

        //THEN
        final boolean applyResult = createPreparedStatementRule.apply("some predicate");
        assertEquals(applyResult, expectedValue);
    }
}