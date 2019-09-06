package com.consol.citrus.db.driver.statement;

import com.consol.citrus.db.driver.JdbcConnection;
import com.consol.citrus.db.driver.JdbcResultSet;
import com.consol.citrus.db.driver.data.CitrusBlob;
import com.consol.citrus.db.driver.data.CitrusClob;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.statement.JdbcCallableStatement;
import com.consol.citrus.db.driver.statement.JdbcStatement;
import com.consol.citrus.db.driver.utils.LobUtils;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.http.client.HttpClient;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.Assert.assertEquals;

public class JdbcCallableStatementTest{

    private HttpClient httpClient;
    private String serverUrl = "localhost";
    private JdbcConnection jdbcConnection;
    private final int TEST_VALUE_INDEX = 2;
    private final String TEST_VALUE_NAME = "col2";
    private LobUtils lobUtils;
    private JdbcCallableStatement callableStatement;

    private JdbcResultSet resultSetSpy;

    @BeforeMethod
    public void setup(){
        httpClient = mock(HttpClient.class);
        jdbcConnection = mock(JdbcConnection.class);
        lobUtils = mock(LobUtils.class);

        callableStatement = generateCallableStatement();

        resultSetSpy = mock(JdbcResultSet.class);
        callableStatement.resultSet = resultSetSpy;
    }

    @Test
    public void testRegisterOutParameter() throws SQLException {

        //GIVEN
        final int index = 2;

        //WHEN
        callableStatement.registerOutParameter(index, Types.INTEGER);

        //THEN
        assertEquals(callableStatement.getParameters().get(2), "?");
    }

    @Test
    public void testRegisterOutParameterWithScale() throws SQLException {

        //GIVEN
        final int index = 2;

        //WHEN
        callableStatement.registerOutParameter(index, Types.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get(2), "?");
    }

    @Test
    public void testRegisterOutParameterWithTypeName() throws SQLException {

        //GIVEN
        final int index = 2;

        //WHEN
        callableStatement.registerOutParameter(index, Types.INTEGER, "STRUCT");

        //THEN
        assertEquals(callableStatement.getParameters().get(2), "?");
    }

    @Test
    public void testRegisterOutParameterByName() {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.registerOutParameter(parameterName, Types.INTEGER);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "?");
    }

    @Test
    public void testRegisterOutParameterByNameWithScale() {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.registerOutParameter(parameterName, Types.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "?");
    }

    @Test
    public void testRegisterOutParameterByNameWithTypeName() {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.registerOutParameter(parameterName, Types.INTEGER, "STRUCT");

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "?");
    }

    @Test
    public void testRegisterOutParameterWithSqlType() {

        //GIVEN

        //WHEN
        callableStatement.registerOutParameter(1, JDBCType.INTEGER);

        //THEN
        assertEquals(callableStatement.getParameters().get(1), "?");
    }

    @Test
    public void testRegisterOutParameterWithSqlTypeAndScale() {

        //GIVEN

        //WHEN
        callableStatement.registerOutParameter(1, JDBCType.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get(1), "?");
    }

    @Test
    public void testRegisterOutParameterWithSqlTypeAndTypeName() {

        //GIVEN
        //WHEN
        callableStatement.registerOutParameter(1, JDBCType.INTEGER, "STRUCT");

        //THEN
        assertEquals(callableStatement.getParameters().get(1), "?");
    }

    @Test
    public void testRegisterOutParameterByNameWithSqlType() {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.registerOutParameter(parameterName, JDBCType.INTEGER);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "?");
    }

    @Test
    public void testRegisterOutParameterByNameWithSqlTypeAndScale() {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.registerOutParameter(parameterName, JDBCType.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "?");
    }

    @Test
    public void testRegisterOutParameterByNameWithSqlTypeAndTypeName() {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.registerOutParameter(parameterName, JDBCType.INTEGER, "STRUCT");

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "?");
    }

    @Test
    public void testSetUrlWithName() throws MalformedURLException {

        //GIVEN
        final String parameterName = "MyUrl";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final URL url = new URL("http://www.example.com/datalink/");

        //WHEN
        callableStatement.setURL(parameterName, url);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), url);
    }

    @Test
    public void testSetNullWithName(){

        //GIVEN
        final String parameterName = "myNull";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.setNull(parameterName, Types.NULL);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "null");
    }

    @Test
    public void testSetBooleanWithName(){

        //GIVEN
        final String parameterName = "myBoolean";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.setBoolean(parameterName, true);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), true);
    }

    @Test
    public void testSetByteWithName() {

        //GIVEN
        final String parameterName = "myByte";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final byte myByte = 8;

        //WHEN
        callableStatement.setByte(parameterName, myByte);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myByte);
    }

    @Test
    public void testSetShortWithName() {

        //GIVEN
        final String parameterName = "myShort";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final short myShort = 8;

        //WHEN
        callableStatement.setShort(parameterName, myShort);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myShort);
    }

    @Test
    public void testSetIntWithName() {

        //GIVEN
        final String parameterName = "myInt";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final int myInt = 42;

        //WHEN
        callableStatement.setInt(parameterName, myInt);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myInt);
    }

    @Test
    public void testSetLongWithName() {

        //GIVEN
        final String parameterName = "myLong";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final long myLong = 42L;

        //WHEN
        callableStatement.setLong(parameterName, myLong);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myLong);
    }

    @Test
    public void testSetFloatWithName() {

        //GIVEN
        final String parameterName = "myFloat";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final float myFloat = 4.2F;

        //WHEN
        callableStatement.setFloat(parameterName, myFloat);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myFloat);
    }

    @Test
    public void testSetDoubleWithName() {

        //GIVEN
        final String parameterName = "myDouble";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final double myDouble = 4.2;

        //WHEN
        callableStatement.setDouble(parameterName, myDouble);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myDouble);
    }

    @Test
    public void testSetBigDecimalWithName() {

        //GIVEN
        final String parameterName = "myBigDecimal";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final BigDecimal myBigDecimal = new BigDecimal(42);

        //WHEN
        callableStatement.setBigDecimal(parameterName, myBigDecimal);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myBigDecimal);
    }

    @Test
    public void testSetStringWithName() {

        //GIVEN
        final String parameterName = "myString";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.setString(parameterName, parameterName);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), parameterName);
    }

    @Test
    public void testSetBytesWithName() {

        //GIVEN
        final String parameterName = "myBytes";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final byte[] myBytes = parameterName.getBytes();

        //WHEN
        callableStatement.setBytes(parameterName, myBytes);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myBytes);
    }

    @Test
    public void testSetDateWithName() {

        //GIVEN
        final String parameterName = "myDate";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final Date myDate = new Date(619912800000L);

        //WHEN
        callableStatement.setDate(parameterName, myDate);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myDate);
    }

    @Test
    public void testSetTimeWithName() {

        //GIVEN
        final String parameterName = "myTime";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final Time myDate = new Time(619912812345L);

        //WHEN
        callableStatement.setTime(parameterName, myDate);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myDate);
    }

    @Test
    public void testSetTimeStampWithName() {

        //GIVEN
        final String parameterName = "myTimeStamp";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final Timestamp myTimeStamp = new Timestamp(619912812345L);

        //WHEN
        callableStatement.setTimestamp(parameterName, myTimeStamp);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), myTimeStamp);
    }

    @Test
    public void testGetStringByIndex() throws SQLException {

        //WHEN
        callableStatement.getString(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getString(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetBooleanByIndex() throws SQLException {

        //WHEN
        callableStatement.getBoolean(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getBoolean(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetByteByIndex() throws SQLException {

        //WHEN
        callableStatement.getByte(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getByte(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetBytesByIndex() throws SQLException {

        //WHEN
        callableStatement.getBytes(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getBytes(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetShortByIndex() throws SQLException {

        //WHEN
        callableStatement.getShort(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getShort(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetIntByIndex() throws SQLException {

        //WHEN
        callableStatement.getInt(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getInt(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetLongByIndex() throws SQLException {

        //WHEN
        callableStatement.getLong(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getLong(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetFloatByIndex() throws SQLException {

        //WHEN
        callableStatement.getFloat(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getFloat(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetDoubleByIndex() throws SQLException {

        //WHEN
        callableStatement.getDouble(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getDouble(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetBigDecimalByIndexWithScale() throws Exception {

        //GIVEN
        final BigDecimal bigDecimalMock = mock(BigDecimal.class);
        when(resultSetSpy.getBigDecimal(TEST_VALUE_INDEX)).thenReturn(bigDecimalMock);

        //WHEN
        callableStatement.getBigDecimal(TEST_VALUE_INDEX, 2);

        //THEN
        verify(resultSetSpy).next();
        //noinspection ResultOfMethodCallIgnored
        verify(bigDecimalMock).setScale(2, RoundingMode.HALF_UP);
    }

    @Test
    public void testGetStringByName() throws SQLException {

        //WHEN
        callableStatement.getString(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getString(TEST_VALUE_NAME);
    }

    @Test
    public void testGetBooleanByName() throws SQLException {

        //WHEN
        callableStatement.getBoolean(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getBoolean(TEST_VALUE_NAME);
    }

    @Test
    public void testGetByteByName() throws SQLException {

        //WHEN
        callableStatement.getByte(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getByte(TEST_VALUE_NAME);
    }

    @Test
    public void testGetShortByName() throws SQLException {

        //WHEN
        callableStatement.getShort(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getShort(TEST_VALUE_NAME);
    }

    @Test
    public void testGetIntByName() throws SQLException {

        //WHEN
        callableStatement.getInt(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getInt(TEST_VALUE_NAME);
    }

    @Test
    public void testGetLongByName() throws SQLException {

        //WHEN
        callableStatement.getLong(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getLong(TEST_VALUE_NAME);
    }

    @Test
    public void testGetFloatByName() throws SQLException {

        //WHEN
        callableStatement.getFloat(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getFloat(TEST_VALUE_NAME);
    }

    @Test
    public void testGetDoubleByName() throws SQLException {

        //WHEN
        callableStatement.getDouble(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getDouble(TEST_VALUE_NAME);
    }

    @Test
    public void testGetBytesByName() throws SQLException {

        //WHEN
        callableStatement.getBytes(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getBytes(TEST_VALUE_NAME);
    }

    @Test
    public void testGetObjectByName() throws SQLException {

        //WHEN
        callableStatement.getObject(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getObject(TEST_VALUE_NAME);
    }

    @Test
    public void testGetBigDecimalByName() throws SQLException {

        //WHEN
        callableStatement.getBigDecimal(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getBigDecimal(TEST_VALUE_NAME);
    }

    @Test
    public void testDelegateWasNull() throws SQLException {


        //WHEN
        callableStatement.wasNull();

        //THEN
        verify(resultSetSpy).wasNull();
    }

    @Test
    public void testGetDateByIndex() throws SQLException {

        //WHEN
        callableStatement.getDate(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getDate(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetTimeByIndex() throws SQLException {

        //WHEN
        callableStatement.getTime(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getTime(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetTimestampByIndex() throws SQLException {

        //WHEN
        callableStatement.getTimestamp(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getTimestamp(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetObjectByIndex() throws SQLException {

        //WHEN
        callableStatement.getObject(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getObject(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetDateByName() throws SQLException {

        //WHEN
        callableStatement.getDate(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getDate(TEST_VALUE_NAME);
    }

    @Test
    public void testGetTimeByName() throws SQLException {

        //WHEN
        callableStatement.getTime(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getTime(TEST_VALUE_NAME);
    }

    @Test
    public void testGetTimestampByName() throws SQLException {

        //WHEN
        callableStatement.getTimestamp(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getTimestamp(TEST_VALUE_NAME);
    }

    @Test
    public void testVerifyResultSetHandling() throws SQLException {

        //GIVEN
        when(resultSetSpy.getRow()).thenReturn(0).thenReturn(1);

        //WHEN
        callableStatement.getTimestamp(1);
        callableStatement.getString(2);


        //THEN
        verify(resultSetSpy, times(2)).getRow();
        verify(resultSetSpy, times(1)).next();
    }

    @Test
    public void testSetLimitedClobFromReader() throws Exception {

        //GIVEN
        final String parameterName = "myClob";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        final long desiredLength = 13L;
        when(lobUtils.fitsInInt(desiredLength)).thenReturn(true);

        final CitrusClob expectedClob = mock(CitrusClob.class);
        final StringReader stringReaderMock = mock(StringReader.class);
        when(lobUtils.createClobFromReader(stringReaderMock, (int)desiredLength)).thenReturn(expectedClob);

        //WHEN
        callableStatement.setClob(parameterName, stringReaderMock, desiredLength);

        //THEN
        final Clob storedClob = (Clob) callableStatement.getParameters().get(parameterName);
        assertEquals(storedClob, expectedClob);
    }

    @Test
    public void testNoopIfLengthExceedsInt() throws Exception {

        //GIVEN
        when(lobUtils.fitsInInt(anyLong())).thenReturn(false);

        //WHEN
        callableStatement.setClob("", mock(Reader.class), Long.MAX_VALUE);

        //THEN
        verify(lobUtils, never()).createClobFromReader(any(Reader.class), anyInt());
    }

    @Test
    public void testSetClob() {

        //GIVEN
        final String parameterName = "myClob";
        final CitrusClob expectedClob = mock(CitrusClob.class);

        //WHEN
        callableStatement.setClob(parameterName, expectedClob);

        //THEN
        final CitrusClob storedClob = (CitrusClob) callableStatement.getParameters().get(parameterName);
        assertEquals(storedClob, expectedClob);
    }

    @Test
    public void testSetClobFromReader() throws Exception {

        //GIVEN
        final String parameterName = "myClob";
        final Reader readerMock = Mockito.mock(Reader.class);
        final CitrusClob expectedClob = Mockito.mock(CitrusClob.class);
        Mockito.when(lobUtils.createClobFromReader(readerMock, -1)).thenReturn(expectedClob);

        //WHEN
        callableStatement.setClob(parameterName, readerMock);

        //THEN
        final CitrusClob storedClob = (CitrusClob) callableStatement.getParameters().get(parameterName);
        assertEquals(storedClob, expectedClob);
    }

    @Test
    public void testGetClobByIndex() {

        //WHEN
        callableStatement.getClob(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getClob(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetClobByName() {

        //WHEN
        callableStatement.getClob(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getClob(TEST_VALUE_NAME);
    }

    @Test
    public void testGetBlobByIndex() {

        //WHEN
        callableStatement.getBlob(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getBlob(TEST_VALUE_INDEX);
    }

    @Test
    public void testGetBlobByName() throws SQLException  {

        //WHEN
        callableStatement.getBlob(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).next();
        verify(resultSetSpy).getBlob(TEST_VALUE_NAME);
    }

    @Test
    public void testSetBlob() {

        //GIVEN
        final String parameterName = "myBlob";
        final CitrusBlob expectedBlob = mock(CitrusBlob.class);

        //WHEN
        callableStatement.setBlob(parameterName, expectedBlob);

        //THEN
        final CitrusBlob storedBlob = (CitrusBlob) callableStatement.getParameters().get(parameterName);
        assertEquals(storedBlob, expectedBlob);
    }

    @Test
    public void testSetLimitedBlobFromStreamByName() throws Exception {

        //GIVEN
        final String parameterName = "myBlob";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        final InputStream inputStreamMock = mock(InputStream.class);

        final long desiredLength = 13L;
        when(lobUtils.fitsInInt(desiredLength)).thenReturn(true);

        final CitrusBlob expectedBlob = mock(CitrusBlob.class);
        when(lobUtils.createBlobFromInputStream(inputStreamMock, (int)desiredLength)).thenReturn(expectedBlob);

        //WHEN
        callableStatement.setBlob(parameterName, inputStreamMock, desiredLength);

        //THEN
        final Blob storedClob = (Blob) callableStatement.getParameters().get(parameterName);
        assertEquals(storedClob, expectedBlob);
    }

    @Test
    public void testSetBlobFromInputStream() throws Exception {

        //GIVEN
        final String parameterName = "myBlob";
        final InputStream inputStreamMock = Mockito.mock(InputStream.class);
        final CitrusBlob expectedBlob = Mockito.mock(CitrusBlob.class);
        Mockito.when(lobUtils.createBlobFromInputStream(inputStreamMock, -1)).thenReturn(expectedBlob);

        //WHEN
        callableStatement.setBlob(parameterName, inputStreamMock);

        //THEN
        final CitrusBlob storedClob = (CitrusBlob) callableStatement.getParameters().get(parameterName);
        assertEquals(storedClob, expectedBlob);
    }

    @Test
    public void testIndexedParametersAreOrderedCorrectly() throws SQLException {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement();
        callableStatement.registerOutParameter(3, Types.VARCHAR);
        callableStatement.registerOutParameter(11, Types.VARCHAR);
        final StatementParameters expectedParameter = new StatementParameters();
        expectedParameter.setParameter(3, "foo");
        expectedParameter.setParameter(11, "bar");

        //WHEN
        callableStatement.setString(3,"foo");
        callableStatement.setString(11, "bar");

        //THEN
        assertEquals(callableStatement.getParameters(), expectedParameter);
    }

    @Test
    public void testNamedParametersAreOrderedCorrectly() {

        //GIVEN
        final String parameter1 = "zParam";
        final String parameter2 = "aParam";
        final JdbcCallableStatement callableStatement =
                generateCallableStatementWithParameter(parameter1, parameter2);
        callableStatement.registerOutParameter(parameter1, Types.VARCHAR);
        callableStatement.registerOutParameter(parameter2, Types.VARCHAR);
        final String expectedStatement = "CALL myFunction(:zParam,:aParam) - (foo,bar)";

        //WHEN
        callableStatement.setString(parameter1,"foo");
        callableStatement.setString(parameter2,"bar");

        //THEN
        assertEquals(callableStatement.composeStatement(), expectedStatement);

    }

    @Test
    public void testParametersMixedParametersAreOrderedCorrectly() throws SQLException {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);
        callableStatement.registerOutParameter(2, Types.VARCHAR);
        callableStatement.registerOutParameter(parameterName, Types.VARCHAR);

        final String expectedStatement = "CALL myFunction(:foo,?) - (foobar,bar)";

        //WHEN
        callableStatement.setParameter(2, "bar");
        callableStatement.setParameter(parameterName, "foobar");

        //THEN
        assertEquals(callableStatement.composeStatement(), expectedStatement);
    }

    @Test
    public void testToString(){
        ToStringVerifier
                .forClass(JdbcCallableStatement.class)
                .withIgnoredFields("lobUtils", "statementComposer")//stateless
                .verify();
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(JdbcCallableStatement.class)
                .withPrefabValues(
                        JdbcResultSet.class,
                        new JdbcResultSet(mock(DataSet.class), mock(JdbcStatement.class)),
                        new JdbcResultSet(mock(DataSet.class), mock(JdbcStatement.class)))
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("resultSet")
                .withIgnoredFields("lobUtils")//stateless
                .withIgnoredFields("statementComposer")//stateless
                .verify();
    }

    private JdbcCallableStatement generateCallableStatement() {
        final String statement = "CALL myFunction(?,?)";
        return new JdbcCallableStatement(httpClient, statement, serverUrl, jdbcConnection, lobUtils);
    }

    private JdbcCallableStatement generateCallableStatementWithParameter(final String parameterName) {
        final String statement = "CALL myFunction(:"+parameterName+",?)";
        return new JdbcCallableStatement(httpClient,statement, serverUrl, jdbcConnection, lobUtils);
    }

    private JdbcCallableStatement generateCallableStatementWithParameter(final String parameterName, final String parameterNameTwo) {
        final String statement = "CALL myFunction(:"+parameterName+",:"+parameterNameTwo+")";
        return new JdbcCallableStatement(httpClient,statement, serverUrl, jdbcConnection, lobUtils);
    }
}