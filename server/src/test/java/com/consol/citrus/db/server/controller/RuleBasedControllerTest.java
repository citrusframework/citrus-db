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

import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class RuleBasedControllerTest {

    private SimpleJdbcController simpleJdbcController;
    private RuleBasedController ruleBasedController;

    @BeforeMethod
    public void setUp(){
        simpleJdbcController = mock(SimpleJdbcController.class);
        ruleBasedController = new RuleBasedController(simpleJdbcController);
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
        when(simpleJdbcController.handleQuery(incomingQuery)).thenReturn(expectedDataSet);


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
        final ExecuteUpdateRule executeQueryRule = new ExecuteUpdateRule(ruleMatcher, ruleExecutor);

        //Add rule to Controller
        ruleBasedController.add(executeQueryRule);

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
        when(simpleJdbcController.handleUpdate(incomingQuery)).thenReturn(expectedUpdatedRows);


        //WHEN
        final int updatedRows = ruleBasedController.handleUpdate(incomingQuery);

        //THEN
        assertEquals(updatedRows, expectedUpdatedRows);
    }
}