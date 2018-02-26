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

package com.consol.citrus.db.server.handler;

import com.consol.citrus.db.server.controller.JdbcController;
import com.consol.citrus.db.server.handler.statement.ExecuteUpdateHandler;
import org.testng.annotations.Test;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class ExecuteUpdateHandlerTest {

    private final JdbcController controllerMock = mock(JdbcController.class);
    private final ExecuteUpdateHandler executeUpdateHandler = new ExecuteUpdateHandler(controllerMock);

    @Test
    public void testControllerIsUsed(){

        //GIVEN
        final String body = String.valueOf(UUID.randomUUID());
        final Request requestMock = mock(Request.class);
        when(requestMock.body()).thenReturn(body);

        final Response responseMock = mock(Response.class);

        final int expectedUpdatedRows = 42;
        when(controllerMock.executeUpdate(anyString())).thenReturn(expectedUpdatedRows);

        //WHEN
        final int updatedResult = (int) executeUpdateHandler.handle(requestMock, responseMock);

        //THEN
        verify(controllerMock).executeUpdate(body);
        assertEquals(updatedResult, expectedUpdatedRows);
    }

}