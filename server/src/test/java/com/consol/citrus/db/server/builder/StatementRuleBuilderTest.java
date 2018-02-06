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
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class StatementRuleBuilderTest {

    private final RuleBasedController ruleBasedControllerMock = mock(RuleBasedController.class);
    private StatementRuleBuilder statementRuleBuilder = new StatementRuleBuilder(ruleBasedControllerMock);


    @Test
    public void testCreate(){

        //GIVEN

        //WHEN
        final CreateStatementRuleBuilder createStatementRuleBuilder = statementRuleBuilder.create();

        //THEN
        assertEquals(createStatementRuleBuilder.getController(), ruleBasedControllerMock);
    }

    @Test
    public void testPrepareMatchesAll(){

        //GIVEN

        //WHEN
        final CreatePreparedStatementRuleBuilder createStatementRuleBuilder = statementRuleBuilder.prepare();

        //THEN
        assertEquals(createStatementRuleBuilder.getController(), ruleBasedControllerMock);
        assertEquals(createStatementRuleBuilder.getPrecondition(), Precondition.matchAll());
    }

    @Test
    public void testPrepareMatchesSql(){

        //GIVEN
        final String sql = "some Statement";

        //WHEN
        final CreatePreparedStatementRuleBuilder createStatementRuleBuilder = statementRuleBuilder.prepare(sql);

        //THEN
        assertEquals(createStatementRuleBuilder.getController(), ruleBasedControllerMock);
        assertTrue(createStatementRuleBuilder.getPrecondition().match(sql));
    }

    @Test
    public void testPrepareMatchesMatcher(){

        //GIVEN
        final String sql = "some Statement";
        final Precondition<String> expectedPrecondition = (statement) -> statement.equals(sql);


        //WHEN
        final CreatePreparedStatementRuleBuilder createStatementRuleBuilder =
                statementRuleBuilder.prepare(expectedPrecondition);

        //THEN
        assertEquals(createStatementRuleBuilder.getController(), ruleBasedControllerMock);
        assertTrue(createStatementRuleBuilder.getPrecondition().match(sql));
    }

    @Test
    public void testExecuteQueryWithString(){

        //GIVEN
        final String sql = "some Statement";


        //WHEN
        final ExecuteQueryRuleBuilder executeQueryRuleBuilder = statementRuleBuilder.executeQuery(sql);

        //THEN
        assertEquals(executeQueryRuleBuilder.getController(), ruleBasedControllerMock);
        assertTrue(executeQueryRuleBuilder.getPrecondition().match(sql));
    }

    @Test
    public void testExecuteQueryWithPattern(){

        //GIVEN
        final Pattern sql = Pattern.compile("some .* pattern");


        //WHEN
        final ExecuteQueryRuleBuilder executeQueryRuleBuilder = statementRuleBuilder.executeQuery(sql);

        //THEN
        assertEquals(executeQueryRuleBuilder.getController(), ruleBasedControllerMock);
        assertTrue(executeQueryRuleBuilder.getPrecondition().match("some cool pattern"));
    }

    @Test
    public void testExecuteUpdateWithString(){

        //GIVEN
        final String sql = "some Statement";


        //WHEN
        final ExecuteUpdateRuleBuilder executeUpdateRuleBuilder = statementRuleBuilder.executeUpdate(sql);

        //THEN
        assertEquals(executeUpdateRuleBuilder.getController(), ruleBasedControllerMock);
        assertTrue(executeUpdateRuleBuilder.getPrecondition().match(sql));
    }

    @Test
    public void testExecuteUpdateWithPattern(){

        //GIVEN
        final Pattern sql = Pattern.compile("some .* pattern");


        //WHEN
        final ExecuteUpdateRuleBuilder executeUpdateRuleBuilder = statementRuleBuilder.executeUpdate(sql);

        //THEN
        assertEquals(executeUpdateRuleBuilder.getController(), ruleBasedControllerMock);
        assertTrue(executeUpdateRuleBuilder.getPrecondition().match("some cool pattern"));
    }

    @Test
    public void testClose(){

        //GIVEN

        //WHEN
        final CloseStatementRuleBuilder closeStatementRuleBuilder = statementRuleBuilder.close();

        //THEN
        assertEquals(closeStatementRuleBuilder.getController(), ruleBasedControllerMock);
    }
}