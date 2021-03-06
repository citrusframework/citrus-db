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
import com.consol.citrus.db.server.handler.statement.CreateStatementHandler;
import org.testng.annotations.Test;
import spark.Request;
import spark.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateStatementHandlerTest {

    private final JdbcController controllerMock = mock(JdbcController.class);
    private final CreateStatementHandler createStatementHandler = new CreateStatementHandler(controllerMock);

    @Test
    public void testControllerIsUsed() throws Exception {

        //GIVEN
        final Request requestMock = mock(Request.class);
        final Response responseMock = mock(Response.class);

        //WHEN
        createStatementHandler.handle(requestMock, responseMock);

        //THEN
        verify(controllerMock).createStatement();
    }
}