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
import com.consol.citrus.db.server.rules.ExecuteUpdateRule;
import com.consol.citrus.db.server.rules.Precondition;
import org.testng.annotations.Test;

import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

public class ExecuteUpdateRuleBuilderTest {

    private Precondition<String> precondition = Precondition.matchAll();
    private RuleBasedController ruleBasedControllerMock = mock(RuleBasedController.class);
    private ExecuteUpdateRuleBuilder builder = new ExecuteUpdateRuleBuilder(precondition, ruleBasedControllerMock);

    @Test
    public void thenReturnWithValueSpecified(){

        //GIVEN
        final int expectedRowsUpdated = new Random().nextInt();

        //WHEN
        final ExecuteUpdateRule executeUpdateRule = builder.thenReturn(expectedRowsUpdated);

        //THEN
        verify(ruleBasedControllerMock).add(executeUpdateRule);
        final int rowsUpdated = executeUpdateRule.apply("predicate");
        assertEquals(rowsUpdated, expectedRowsUpdated);
    }

    @Test
    public void thenReturn(){

        //GIVEN

        //WHEN
        final ExecuteUpdateRule executeUpdateRule = builder.thenReturn();

        //THEN
        verify(ruleBasedControllerMock).add(executeUpdateRule);
        final int rowsUpdated = executeUpdateRule.apply("predicate");
        assertEquals(rowsUpdated, 0);
    }

    @Test(expectedExceptions = JdbcServerException.class)
    public void testThenThrow() {

        //GIVEN
        final JdbcServerException jdbcServerException = new JdbcServerException();

        //WHEN
        final ExecuteUpdateRule rule = builder.thenThrow(jdbcServerException);

        //THEN
        verify(ruleBasedControllerMock).add(rule);
        rule.apply("whatever");
    }
}