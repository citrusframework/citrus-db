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

package com.consol.citrus.db.server.controller;

import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.server.rules.ExecuteQueryRule;
import com.consol.citrus.db.server.rules.ExecuteUpdateRule;
import com.consol.citrus.db.server.rules.RuleExecutor;
import com.consol.citrus.db.server.rules.RuleMatcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class RuleBasedControllerTest {

    private AbstractJdbcController jdbcControllerMock;
    private RuleBasedController ruleBasedController;

    @BeforeMethod
    public void setUp(){
        jdbcControllerMock = mock(AbstractJdbcController.class);
        ruleBasedController = new RuleBasedController(jdbcControllerMock);
    }

    @Test
    public void testHandleQueryWithMatch(){

        //GIVEN
        final String incomingQuery = "some query";

        //Create matcher
        final RuleMatcher<String> ruleMatcher = (RuleMatcher<String>) mock(RuleMatcher.class);
        when(ruleMatcher.match(incomingQuery)).thenReturn(true);

        //Prepare return value for match
        final DataSet expectedDataSet = mock(DataSet.class);
        final RuleExecutor<String, DataSet> ruleExecutor = (RuleExecutor<String, DataSet>) mock(RuleExecutor.class);
        when(ruleExecutor.then(incomingQuery)).thenReturn(expectedDataSet);

        //Compose Rule
        final ExecuteQueryRule executeQueryRule = new ExecuteQueryRule(ruleMatcher, ruleExecutor);

        //Add rule to Controller
        ruleBasedController.add(executeQueryRule);

        //WHEN
        final DataSet dataSet = ruleBasedController.handleQuery(incomingQuery);

        //THEN
        assertEquals(dataSet, expectedDataSet);
    }

    @Test
    public void testHandleQueryWithoutMatch(){

        //GIVEN
        final String incomingQuery = "some query";
        final DataSet expectedDataSet = mock(DataSet.class);
        when(jdbcControllerMock.handleQuery(incomingQuery)).thenReturn(expectedDataSet);


        //WHEN
        final DataSet dataSet = ruleBasedController.handleQuery(incomingQuery);

        //THEN
        assertEquals(dataSet, expectedDataSet);
    }

    @Test
    public void testHandleUpdateWithMatch(){

        //GIVEN
        final String incomingQuery = "some query";

        //Create matcher
        final RuleMatcher<String> ruleMatcher = (RuleMatcher<String>) mock(RuleMatcher.class);
        when(ruleMatcher.match(incomingQuery)).thenReturn(true);

        //Prepare return value for match
        final int expectedUpdatedRows = new Random().nextInt();
        final RuleExecutor<String, Integer> ruleExecutor = (RuleExecutor<String, Integer>) mock(RuleExecutor.class);
        when(ruleExecutor.then(incomingQuery)).thenReturn(expectedUpdatedRows);

        //Compose Rule
        final ExecuteUpdateRule executeUpdateRule = new ExecuteUpdateRule(ruleMatcher, ruleExecutor);

        //Add rule to Controller
        ruleBasedController.add(executeUpdateRule);

        //WHEN
        final int updatedRows = ruleBasedController.handleUpdate(incomingQuery);

        //THEN
        assertEquals(updatedRows, expectedUpdatedRows);
    }

    @Test
    public void testHandleUpdateWithoutMatch(){

        //GIVEN
        final String incomingQuery = "some query";
        final int expectedUpdatedRows = new Random().nextInt();
        when(jdbcControllerMock.handleUpdate(incomingQuery)).thenReturn(expectedUpdatedRows);


        //WHEN
        final int updatedRows = ruleBasedController.handleUpdate(incomingQuery);

        //THEN
        assertEquals(updatedRows, expectedUpdatedRows);
    }

    @Test
    public void testOpenConnectionDelegation(){

        //GIVEN
        final Map<String, String> properties = Collections.singletonMap("name", "value");

        //WHEN
        ruleBasedController.openConnection(properties);

        //THEN
        verify(jdbcControllerMock).openConnection(properties);
    }

    @Test
    public void testCloseConnectionDelegation(){

        //GIVEN

        //WHEN
        ruleBasedController.closeConnection();

        //THEN
        verify(jdbcControllerMock).closeConnection();
    }

    @Test
    public void testCreateStatementDelegation(){

        //GIVEN

        //WHEN
        ruleBasedController.createStatement();

        //THEN
        verify(jdbcControllerMock).createStatement();
    }

    @Test
    public void testCreatePreparedStatementDelegation(){

        //GIVEN
        final String sql = "some query";

        //WHEN
        ruleBasedController.createPreparedStatement(sql);

        //THEN
        verify(jdbcControllerMock).createPreparedStatement(sql);
    }

    @Test
    public void testCloseStatementDelegation(){

        //GIVEN

        //WHEN
        ruleBasedController.closeStatement();

        //THEN
        verify(jdbcControllerMock).closeStatement();
    }

    @Test
    public void testSetTransactionStateDelegation(){

        //GIVEN
        final boolean transactionState = new Random().nextBoolean();

        //WHEN
        ruleBasedController.setTransactionState(transactionState);

        //THEN
        verify(jdbcControllerMock).setTransactionState(transactionState);
    }

    @Test
    public void testCommitStatementsDelegation(){

        //GIVEN

        //WHEN
        ruleBasedController.commitStatements();

        //THEN
        verify(jdbcControllerMock).commitStatements();
    }

}