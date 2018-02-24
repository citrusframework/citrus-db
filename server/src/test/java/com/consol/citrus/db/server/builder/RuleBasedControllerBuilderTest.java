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
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class RuleBasedControllerBuilderTest {

    private final RuleBasedController ruleBasedController = mock(RuleBasedController.class);
    private final RuleBasedControllerBuilder ruleBasedControllerBuilder =
            new RuleBasedControllerBuilder(ruleBasedController);

    @Test
    public void testConnection(){

        //GIVEN

        //WHEN
        final ConnectionRuleBuilder connectionRuleBuilder = ruleBasedControllerBuilder.connection();

        //THEN
        Assert.assertEquals(connectionRuleBuilder.getController(), ruleBasedController);
    }

    @Test
    public void testStatement(){

        //GIVEN

        //WHEN
        final StatementRuleBuilder statementRuleBuilder = ruleBasedControllerBuilder.statement();

        //THEN
        Assert.assertEquals(statementRuleBuilder.getController(), ruleBasedController);
    }

}