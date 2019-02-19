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

import com.consol.citrus.db.driver.dataset.DataSet;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
public class JdbcStatementTest {

    private JdbcStatement jdbcStatement;
    private HttpClient httpClient;
    private final String responsePayload = "[{ \"foo\": \"bar\" }]";

    private StatusLine statusLine;
    private HttpEntity httpEntity;

    @SuppressWarnings("Duplicates")
    @BeforeMethod
    public void setup() throws Exception{
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final JdbcConnection connection = mock(JdbcConnection.class);
        final String serverUrl = "db.klingon-empire.kr";

        httpClient = mock(HttpClient.class);
        statusLine = mock(StatusLine.class);
        httpEntity = mock(HttpEntity.class);

        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getEntity()).thenReturn(httpEntity);

        jdbcStatement = new JdbcStatement(httpClient, serverUrl, connection);
    }

    @Test
    public void testExecuteQuery() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpEntity.getContentType())
                .thenReturn(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        when(httpEntity.getContent())
                .thenReturn(new ByteArrayInputStream(responsePayload.getBytes()));

        //WHEN
        final ResultSet resultSet = jdbcStatement.executeQuery("SELECT something FROM somewhere");

        //THEN
        assertTrue(resultSet.next());
        assertEquals(resultSet.getMetaData().getColumnLabel(1), "foo");
        assertEquals(resultSet.getString(1), "bar");
    }

    @Test(expectedExceptions = SQLException.class)
    public void testExecuteQueryHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcStatement.executeQuery("query");

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testExecuteQueryWithUnknownContentType() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpEntity.getContentType())
                .thenReturn(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/unknown"));

        //WHEN
        jdbcStatement.executeQuery("query");

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testExecuteQueryIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcStatement.executeQuery("query");

        //THEN
        //Exception is thrown
    }

    @Test
    public void testExecuteUpdate() throws Exception{

        //GIVEN
        final String responsePayload = "42";
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpEntity.getContent())
                .thenReturn(new ByteArrayInputStream(responsePayload.getBytes()));

        //WHEN
        final int updatedRows = jdbcStatement.executeUpdate("update");

        //THEN
        assertEquals(42, updatedRows);
    }

    @Test(expectedExceptions = SQLException.class)
    public void testExecuteUpdateHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcStatement.executeUpdate("update");

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testExecuteUpdateIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcStatement.executeUpdate("update");

        //THEN
        //Exception is thrown
    }

    @Test
    public void testExecute() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpEntity.getContentType())
                .thenReturn(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        when(httpEntity.getContent())
                .thenReturn(new ByteArrayInputStream(responsePayload.getBytes()));

        //WHEN
        final boolean isResultSet = jdbcStatement.execute("statement");

        //THEN
        assertTrue(isResultSet);
    }

    @Test(expectedExceptions = SQLException.class)
    public void testExecuteHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcStatement.execute("statement");

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testExecuteIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcStatement.execute("statement");

        //THEN
        //Exception is thrown
    }

    @Test
    public void testClose() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);
        assertFalse(jdbcStatement.isClosed());

        //WHEN
        jdbcStatement.close();

        //THEN
        assertTrue(jdbcStatement.isClosed());
    }

    @Test
    public void testCloseAlsoClosesResultSet() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200).thenReturn(200);
        when(httpEntity.getContentType())
                .thenReturn(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        when(httpEntity.getContent())
                .thenReturn(new ByteArrayInputStream(responsePayload.getBytes()));
        jdbcStatement.execute("SELECT foo FROM bar");
        assertFalse(jdbcStatement.getResultSet().isClosed());

        //WHEN
        jdbcStatement.close();

        //THEN
        assertTrue(jdbcStatement.getResultSet().isClosed());
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCloseHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcStatement.close();

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCloseIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcStatement.close();

        //THEN
        //Exception is thrown
    }

    @Test
    public void testAddBatchStatement() throws SQLException {

        //GIVEN
        final String statementOne = "SELECT * FROM somewhere";
        final String statementTwo = "SELECT * FROM somewhereElse";

        //WHEN
        jdbcStatement.addBatch(statementOne);
        jdbcStatement.addBatch(statementTwo);

        //THEN
        assertEquals(jdbcStatement.getBatchStatements().get(0), statementOne);
        assertEquals(jdbcStatement.getBatchStatements().get(1), statementTwo);
    }

    @Test(expectedExceptions = SQLException.class)
    public void testAddBatchThrowsExceptionOnClosedStatement() throws SQLException {

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);
        jdbcStatement.close();

        //WHEN
        jdbcStatement.addBatch("some statement");

        //THEN
        //exception is thrown
    }

    @Test
    public void testClearBatchStatement() throws SQLException {

        //GIVEN
        jdbcStatement.addBatch("SELECT * FROM somewhere");

        //WHEN
        jdbcStatement.clearBatch();

        //THEN
        assertEquals(jdbcStatement.getBatchStatements().size(), 0);
    }

    @Test(expectedExceptions = SQLException.class)
    public void testClearBatchThrowsExceptionOnClosedStatement() throws SQLException {

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);
        jdbcStatement.close();

        //WHEN
        jdbcStatement.clearBatch();

        //THEN
        //exception is thrown
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(JdbcStatement.class).verify();
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(JdbcStatement.class)
                .withPrefabValues(
                        JdbcResultSet.class,
                        new JdbcResultSet(PowerMockito.mock(DataSet.class), PowerMockito.mock(JdbcStatement.class)),
                        new JdbcResultSet(PowerMockito.mock(DataSet.class), PowerMockito.mock(JdbcStatement.class)))
                .withRedefinedSubclass(JdbcPreparedStatement.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("resultSet")
                .verify();
    }
}