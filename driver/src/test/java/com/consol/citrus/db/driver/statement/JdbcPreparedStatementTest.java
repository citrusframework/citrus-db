/*
 *  Copyright 2006-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.consol.citrus.db.driver.statement;

import com.consol.citrus.db.driver.JdbcConnection;
import com.consol.citrus.db.driver.JdbcResultSet;
import com.consol.citrus.db.driver.data.CitrusBlob;
import com.consol.citrus.db.driver.data.CitrusClob;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.utils.LobUtils;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.http.client.HttpClient;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class JdbcPreparedStatementTest {

    private final HttpClient httpClientMock = mock(HttpClient.class);
    private final JdbcConnection jdbcConnectionMock = mock(JdbcConnection.class);
    private LobUtils lobUtils;
    private StatementParameters statementParameters;

    private JdbcPreparedStatement jdbcPreparedStatement;

    @BeforeMethod
    public void setUp() {
        lobUtils = PowerMockito.mock(LobUtils.class);
        statementParameters = spy(StatementParameters.class);
        jdbcPreparedStatement = spy(new JdbcPreparedStatement(
                httpClientMock,
                "SELECT id, name FROM airports WHERE name = ?",
                "url",
                jdbcConnectionMock,
                lobUtils,
                statementParameters));
    }


    @Test
    public void testSetParameter() {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().get(1), 2);
    }

    @Test
    public void testSetParameterAddAnotherValue(){

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);
        jdbcPreparedStatement.setParameter(2, 42);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().size(), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get(1), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get(2), 42);
    }

    @Test
    public void testSetParameterOverwritesValue() {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);
        jdbcPreparedStatement.setParameter(1, 42);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().size(), 1);
        assertEquals(jdbcPreparedStatement.getParameters().get(1), 42);
    }

    @Test
    public void testParameterOrderIsPreserved(){

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(2, 42);
        jdbcPreparedStatement.setParameter(1, 2);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().size(), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get(1), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get(2), 42);
    }

    @Test
    public void textExecuteBatchedPreparedStatements() throws SQLException {

        //GIVEN
        jdbcPreparedStatement.setString(1, "MUC");
        jdbcPreparedStatement.addBatch();

        jdbcPreparedStatement.setString(1, "DUS");
        jdbcPreparedStatement.addBatch();

        doReturn(false).when(jdbcPreparedStatement).execute(any());
        when(jdbcPreparedStatement.getUpdateCount()).thenReturn(42).thenReturn(84);

        final int[] expectedUpdateCounts = new int[]{42,84};

        //WHEN
        final int[] updateCounts = jdbcPreparedStatement.executeBatch();

        //THEN
        verify(jdbcPreparedStatement).execute("SELECT id, name FROM airports WHERE name = ? - (MUC)");
        verify(jdbcPreparedStatement).execute("SELECT id, name FROM airports WHERE name = ? - (DUS)");
        assertEquals(updateCounts, expectedUpdateCounts);
    }

    @Test(expectedExceptions = SQLException.class)
    public void textAddBatchWithSqlThrowsException() throws SQLException {

        //WHEN
        jdbcPreparedStatement.addBatch("some statement");

        //THEN
        //exception is thrown
    }

    @Test
    public void testSetClobWithParameterIndexAndClob() {

        //GIVEN
        final Clob clob = mock(Clob.class);

        //WHEN
        jdbcPreparedStatement.setClob(5, clob);

        //THEN
        verify(jdbcPreparedStatement).setParameter(5, clob);
    }

    @Test
    public void testSetLimitedClobFromReader() throws Exception {

        //GIVEN
        final long desiredLength = 13L;
        when(lobUtils.fitsInInt(desiredLength)).thenReturn(true);

        final CitrusClob expectedClob = mock(CitrusClob.class);
        final StringReader stringReaderMock = mock(StringReader.class);
        when(lobUtils.createClobFromReader(stringReaderMock, (int)desiredLength)).thenReturn(expectedClob);

        //WHEN
        jdbcPreparedStatement.setClob(12, stringReaderMock, desiredLength);

        //THEN
        verify(jdbcPreparedStatement).setParameter(12, expectedClob);
    }

    @Test
    public void TestNoopIfLengthExceedsInt() throws Exception {

        //GIVEN
        PowerMockito.when(lobUtils.fitsInInt(anyLong())).thenReturn(false);

        //WHEN
        jdbcPreparedStatement.setClob(12, PowerMockito.mock(Reader.class), Long.MAX_VALUE);

        //THEN
        verify(lobUtils, never()).createClobFromReader(any(Reader.class), anyInt());
    }

    @Test
    public void setClobFromReader() throws Exception {

        //GIVEN
        final Reader readerMock = mock(Reader.class);
        final CitrusClob citrusClobMock = mock(CitrusClob.class);
        when(lobUtils.createClobFromReader(readerMock, -1)).thenReturn(citrusClobMock);

        //WHEN
        jdbcPreparedStatement.setClob(5, readerMock);

        //THEN
        verify(jdbcPreparedStatement).setParameter(5, citrusClobMock);
    }

    @Test
    public void testSetBlobWithParameterIndexAndClob() {

        //GIVEN
        final Blob blob = mock(Blob.class);

        //WHEN
        jdbcPreparedStatement.setBlob(5, blob);

        //THEN
        verify(jdbcPreparedStatement).setParameter(5, blob);
    }

    @Test
    public void testSetLimitedBlobFromInputStream() throws Exception {

        //GIVEN
        final long desiredLength = 13L;
        when(lobUtils.fitsInInt(desiredLength)).thenReturn(true);

        final CitrusBlob expectedBlob = mock(CitrusBlob.class);
        final InputStream inputStreamMock = mock(InputStream.class);
        when(lobUtils.createBlobFromInputStream(inputStreamMock, (int)desiredLength)).thenReturn(expectedBlob);

        //WHEN
        jdbcPreparedStatement.setBlob(12, inputStreamMock, desiredLength);

        //THEN
        verify(jdbcPreparedStatement).setParameter(12, expectedBlob);
    }

    @Test
    public void setBlobFromInputStream() throws Exception {

        //GIVEN
        final InputStream inputStreamMock = mock(InputStream.class);
        final CitrusBlob citrusBlobMock = mock(CitrusBlob.class);
        when(lobUtils.createBlobFromInputStream(inputStreamMock, -1)).thenReturn(citrusBlobMock);

        //WHEN
        jdbcPreparedStatement.setBlob(5, inputStreamMock);

        //THEN
        verify(jdbcPreparedStatement).setParameter(5, citrusBlobMock);
    }

    @Test
    public void testParametersAreOrderedCorrectly() {

        //GIVEN
        final StatementParameters expectedParameter = new StatementParameters();
        expectedParameter.setParameter(3, "foo");
        expectedParameter.setParameter(11, "bar");

        //WHEN
        jdbcPreparedStatement.setString(3,"foo");
        jdbcPreparedStatement.setString(11, "bar");

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters(), expectedParameter);
    }

    @Test
    public void testSetNull(){

        //GIVEN
        final int index = getRandomIndex();

        //WHEN
        jdbcPreparedStatement.setNull(index, Types.NULL);

        //THEN
        verify(statementParameters).setParameter(index, null);
    }

    @Test
    public void testSetBoolean(){

        //GIVEN
        final int index = getRandomIndex();
        final boolean value = true;

        //WHEN
        jdbcPreparedStatement.setBoolean(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetByte(){

        //GIVEN
        final int index = getRandomIndex();
        final byte value = 8;

        //WHEN
        jdbcPreparedStatement.setByte(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetShort(){

        //GIVEN
        final int index = getRandomIndex();
        final short value = 7;

        //WHEN
        jdbcPreparedStatement.setShort(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetInt(){

        //GIVEN
        final int index = getRandomIndex();
        final int value = 42;

        //WHEN
        jdbcPreparedStatement.setInt(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetLong(){

        //GIVEN
        final int index = getRandomIndex();
        final long value = 84L;

        //WHEN
        jdbcPreparedStatement.setLong(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetFloat(){

        //GIVEN
        final int index = getRandomIndex();
        final float value = 42.42F;

        //WHEN
        jdbcPreparedStatement.setFloat(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetDouble(){

        //GIVEN
        final int index = getRandomIndex();
        final double value = 84.84;

        //WHEN
        jdbcPreparedStatement.setDouble(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetBigDecimal(){

        //GIVEN
        final int index = getRandomIndex();
        final BigDecimal value = new BigDecimal(Integer.MAX_VALUE);

        //WHEN
        jdbcPreparedStatement.setBigDecimal(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetString(){

        //GIVEN
        final int index = getRandomIndex();
        final String value = "42";

        //WHEN
        jdbcPreparedStatement.setString(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetBytes(){

        //GIVEN
        final int index = getRandomIndex();
        final byte[] value = "42".getBytes();

        //WHEN
        jdbcPreparedStatement.setBytes(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetDate(){

        //GIVEN
        final int index = getRandomIndex();
        final Date value = Date.valueOf("2019-09-09");

        //WHEN
        jdbcPreparedStatement.setDate(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetTime(){

        //GIVEN
        final int index = getRandomIndex();
        final Time value = Time.valueOf("09:25:00");

        //WHEN
        jdbcPreparedStatement.setTime(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetTimestamp(){

        //GIVEN
        final int index = getRandomIndex();
        final Timestamp value = Timestamp.valueOf("2019-09-09 09:25:00");

        //WHEN
        jdbcPreparedStatement.setTimestamp(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetAsciiStream() {

        //GIVEN
        final int index = getRandomIndex();
        final InputStream value = new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        };

        //WHEN
        jdbcPreparedStatement.setAsciiStream(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetAsciiStreamWithLength(){

        //GIVEN
        final int index = getRandomIndex();
        final InputStream value = new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        };

        //WHEN
        jdbcPreparedStatement.setAsciiStream(index, value, 42);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetUnicodeStreamWithLength(){

        //GIVEN
        final int index = getRandomIndex();
        final InputStream value = new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        };

        //WHEN
        jdbcPreparedStatement.setUnicodeStream(index, value, 42);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetBinaryStreamWithLength(){

        //GIVEN
        final int index = getRandomIndex();
        final InputStream value = new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        };

        //WHEN
        jdbcPreparedStatement.setBinaryStream(index, value, 42);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testClear(){

        //WHEN
        jdbcPreparedStatement.clearParameters();

        //THEN
        verify(statementParameters).clear();
    }

    @Test
    public void testSetObjectWithIndex(){

        //GIVEN
        final int index = getRandomIndex();
        final Object value = new Object();

        //WHEN
        jdbcPreparedStatement.setObject(index, value, 42);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testSetObject(){

        //GIVEN
        final int index = getRandomIndex();
        final Object value = new Object();

        //WHEN
        jdbcPreparedStatement.setObject(index, value);

        //THEN
        verify(statementParameters).setParameter(index, value);
    }

    @Test
    public void testToString(){
        ToStringVerifier
                .forClass(JdbcPreparedStatement.class)
                .withIgnoredFields("lobUtils", "statementComposer")//stateless
                .verify();
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(JdbcPreparedStatement.class)
                .withPrefabValues(
                        JdbcResultSet.class,
                        new JdbcResultSet(PowerMockito.mock(DataSet.class), PowerMockito.mock(JdbcStatement.class)),
                        new JdbcResultSet(PowerMockito.mock(DataSet.class), PowerMockito.mock(JdbcStatement.class)))
                .withRedefinedSuperclass()
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("resultSet")
                .withIgnoredFields("lobUtils")//stateless
                .withIgnoredFields("statementComposer")//stateless
                .verify();
    }

    private int getRandomIndex() {
        return (int)(Math.random()*10);
    }
}