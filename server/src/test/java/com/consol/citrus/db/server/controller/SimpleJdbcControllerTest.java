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
import com.consol.citrus.db.driver.dataset.DataSetProducer;
import com.consol.citrus.db.driver.exchange.DatabaseResult;
import com.consol.citrus.db.server.JdbcServerException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class SimpleJdbcControllerTest {

    private DataSetProducer dataSetProducerMock;
    private SimpleJdbcController simpleJdbcController;

    @BeforeMethod
    public void setUp(){
        dataSetProducerMock = mock(DataSetProducer.class);
        simpleJdbcController = new SimpleJdbcController(dataSetProducerMock);
    }

    @Test
    public void testHandleQueryDelegatesToDataSetProducer() throws Exception{

        //GIVEN
        final DataSet expectedDataSet = mock(DataSet.class);
        when(dataSetProducerMock.produce()).thenReturn(expectedDataSet);

        //WHEN
        final DatabaseResult databaseResult = simpleJdbcController.handleQuery("Some statement");

        //THEN
        verify(dataSetProducerMock).produce();
        assertEquals(databaseResult.getDataSet(), expectedDataSet);
    }

    @Test(expectedExceptions = JdbcServerException.class)
    public void testExceptionHandlingOfHandleQuery() throws Exception{

        //GIVEN
        when(dataSetProducerMock.produce()).thenThrow(SQLException.class);

        //WHEN
        simpleJdbcController.handleQuery("Some statement");

        //THEN
    }

    @Test
    public void testHandleUpdateReturnsFixedValue() {

        //GIVEN
        final int expectedAffectedRows = 0;

        //WHEN
        final int affectedRows =  simpleJdbcController.handleUpdate("Some statement");

        //THEN
        assertEquals(affectedRows, expectedAffectedRows);
    }
}