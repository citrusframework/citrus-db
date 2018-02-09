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

package com.consol.citrus.db.driver;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class JdbcConnectionTest {

    private final HttpClient httpClient = mock(HttpClient.class);
    private final String serverUrl = "db.klingon-empire.kr";
    private final JdbcConnection jdbcConnection = new JdbcConnection(httpClient, serverUrl);

    @BeforeMethod
    public void resetMocks(){
        reset(httpClient);
    }

    @Test
    public void testCreateStatement() throws Exception{

        //GIVEN
        final JdbcStatement expectedStatement = new JdbcStatement(httpClient, serverUrl);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpClient.execute(any())).thenReturn(httpResponse);


        final StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);

        //WHEN
        final Statement statement = jdbcConnection.createStatement();

        //THEN
        Assert.assertEquals(statement, expectedStatement);
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCreateStatementHttpCallFailed() throws Exception{

        //GIVEN
        final HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(mock(HttpEntity.class));

        final StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);

        //WHEN
        jdbcConnection.createStatement();

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCreateStatementIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcConnection.createStatement();

        //THEN
        //Exception is thrown
    }
}