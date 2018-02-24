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

package com.consol.citrus.db.server.exceptionhandler;

import com.consol.citrus.db.server.JdbcServerException;
import org.testng.annotations.Test;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JdbcServerExceptionHandlerTest {

    private JdbcServerExceptionHandler jdbcServerExceptionHandler = new JdbcServerExceptionHandler();

    @Test
    public void testJdbcServerExceptionHandling(){

        //GIVEN
        final String message  = UUID.randomUUID().toString();
        final JdbcServerException jdbcServerExceptionMock = mock(JdbcServerException.class);
        final Request requestMock = mock(Request.class);
        final Response responseMock = mock(Response.class);

        when(jdbcServerExceptionMock.getMessage()).thenReturn(message);

        //WHEN
        jdbcServerExceptionHandler.handle(jdbcServerExceptionMock, requestMock, responseMock);

        //THEN
        verify(responseMock).status(500);
        verify(responseMock).body(message);
    }
}