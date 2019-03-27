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

import com.consol.citrus.db.driver.data.CitrusBlob;
import com.consol.citrus.db.driver.data.CitrusClob;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
public class JdbcConnectionTest {

    private final HttpClient httpClient = mock(HttpClient.class);
    private final String serverUrl = "db.klingon-empire.kr";
    private final JdbcConnection jdbcConnection = new JdbcConnection(httpClient, serverUrl);

    private final HttpResponse httpResponse = mock(HttpResponse.class);
    private final StatusLine statusLine = mock(StatusLine.class);
    private final HttpEntity httpEntity = mock(HttpEntity.class);

    @SuppressWarnings("Duplicates")
    @BeforeMethod
    public void setup() throws Exception{
        reset(httpClient);
        reset(httpResponse);
        reset(statusLine);

        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
    }

    @Test
    public void testCreateStatement() throws Exception{

        //GIVEN
        final JdbcStatement expectedStatement = new JdbcStatement(httpClient, serverUrl, jdbcConnection);
        when(statusLine.getStatusCode()).thenReturn(200);

        //WHEN
        final Statement statement = jdbcConnection.createStatement();

        //THEN
        assertEquals(statement, expectedStatement);
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCreateStatementHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

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

    @Test
    public void testClose() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);

        //WHEN
        jdbcConnection.close();

        //THEN
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCloseHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcConnection.close();

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCloseIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcConnection.close();

        //THEN
        //Exception is thrown
    }

    @Test
    public void testSetAutoCommit() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);

        //WHEN
        jdbcConnection.setAutoCommit(true);

        //THEN
    }

    @Test(expectedExceptions = SQLException.class)
    public void testSetAutoCommitHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcConnection.setAutoCommit(false);

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testSetAutoCommitIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcConnection.setAutoCommit(false);

        //THEN
        //Exception is thrown
    }

    @Test
    public void testGetAutoCommit() throws Exception{

        //GIVEN
        final String transactionState = "true";
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(transactionState.getBytes()));

        //WHEN
        final boolean isAutoCommit = jdbcConnection.getAutoCommit();

        //THEN
        Assert.assertFalse(isAutoCommit);
    }

    @Test(expectedExceptions = SQLException.class)
    public void testGetAutoCommitHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcConnection.getAutoCommit();

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testGetAutoCommitIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcConnection.getAutoCommit();

        //THEN
        //Exception is thrown
    }

    @Test
    public void testCommit() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);

        //WHEN
        jdbcConnection.commit();

        //THEN
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCommitHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcConnection.commit();

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testCommitIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcConnection.commit();

        //THEN
        //Exception is thrown
    }

    @Test
    public void testRollback() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(200);

        //WHEN
        jdbcConnection.rollback();

        //THEN
    }

    @Test(expectedExceptions = SQLException.class)
    public void testRollbackHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcConnection.rollback();

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testRollbackIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcConnection.rollback();

        //THEN
        //Exception is thrown
    }

    @Test
    public void testPrepareStatement() throws Exception {

        //GIVEN
        final String sql = "SELECT something FROM somewhere";
        final JdbcStatement expectedStatement = new JdbcPreparedStatement(httpClient,sql, serverUrl, jdbcConnection);
        when(statusLine.getStatusCode()).thenReturn(200);

        //WHEN
        final Statement statement = jdbcConnection.prepareStatement(sql);

        //THEN
        assertEquals(statement, expectedStatement);
    }

    @Test(expectedExceptions = SQLException.class)
    public void testPrepareStatementHttpCallFailed() throws Exception{

        //GIVEN
        when(statusLine.getStatusCode()).thenReturn(500);

        //WHEN
        jdbcConnection.prepareStatement("sql");

        //THEN
        //Exception is thrown
    }

    @Test(expectedExceptions = SQLException.class)
    public void testPrepareStatementIoExceptionIsWrappedInSqlException() throws Exception{

        //GIVEN
        when(httpClient.execute(any())).thenThrow(IOException.class);

        //WHEN
        jdbcConnection.prepareStatement("sql");

        //THEN
        //Exception is thrown
    }

    @Test
    public void testCreateClob() {

        //GIVEN
        final Clob expectedClob = new CitrusClob();

        //WHEN
        final Clob clob = jdbcConnection.createClob();

        //THEN
        assertEquals(clob, expectedClob);
    }

    @Test
    public void testCreateBlob() {

        //GIVEN
        final Blob expectedBlob = new CitrusBlob();

        //WHEN
        final Blob blob = jdbcConnection.createBlob();

        //THEN
        assertEquals(blob, expectedBlob);
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(JdbcConnection.class).verify();
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(JdbcConnection.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}