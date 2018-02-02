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
import org.testng.annotations.Test;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpenConnectionHandlerTest {

    private final JdbcController controllerMock = mock(JdbcController.class);
    private final OpenConnectionHandler openConnectionHandler = new OpenConnectionHandler(controllerMock);

    @Test
    public void testControllerIsUsed(){

        //GIVEN
        final Request requestMock = mock(Request.class);

        final Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("someKey", "someValue");
        when(requestMock.params()).thenReturn(expectedMap);

        final Response responseMock = mock(Response.class);


        //WHEN
        openConnectionHandler.handle(requestMock, responseMock);

        //THEN
        verify(controllerMock).openConnection(expectedMap);
    }

}