package com.consol.citrus.db.driver;

import com.consol.citrus.db.driver.dataset.DataSet;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.http.client.HttpClient;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

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
    private JdbcCallableStatement callableStatement;

    private JdbcResultSet resultSetSpy;

    @BeforeMethod
    public void setup(){
        httpClient = mock(HttpClient.class);
        jdbcConnection = mock(JdbcConnection.class);

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
        assertEquals(callableStatement.getParameters().get("1"), "?");
    }

    @Test
    public void testRegisterOutParameterWithScale() throws SQLException {

        //GIVEN
        final int index = 2;

        //WHEN
        callableStatement.registerOutParameter(index, Types.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get("1"), "?");
    }

    @Test
    public void testRegisterOutParameterWithTypeName() throws SQLException {

        //GIVEN
        final int index = 2;

        //WHEN
        callableStatement.registerOutParameter(index, Types.INTEGER, "STRUCT");

        //THEN
        assertEquals(callableStatement.getParameters().get("1"), "?");
    }

    @Test
    public void testRegisterOutParameterByName() throws SQLException {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.registerOutParameter(parameterName, Types.INTEGER);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "?");
    }

    @Test
    public void testRegisterOutParameterByNameWithScale() throws SQLException {

        //GIVEN
        final String parameterName = "foo";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.registerOutParameter(parameterName, Types.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "?");
    }

    @Test
    public void testRegisterOutParameterByNameWithTypeName() throws SQLException {

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
        assertEquals(callableStatement.getParameters().get("0"), "?");
    }

    @Test
    public void testRegisterOutParameterWithSqlTypeAndScale() {

        //GIVEN

        //WHEN
        callableStatement.registerOutParameter(1, JDBCType.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get("0"), "?");
    }

    @Test
    public void testRegisterOutParameterWithSqlTypeAndTypeName() {

        //GIVEN
        //WHEN
        callableStatement.registerOutParameter(1, JDBCType.INTEGER, "STRUCT");

        //THEN
        assertEquals(callableStatement.getParameters().get("0"), "?");
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
    public void testSetUrlWithName() throws SQLException, MalformedURLException {

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
    public void testSetNullWithName() throws SQLException {

        //GIVEN
        final String parameterName = "myNull";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.setNull(parameterName, Types.NULL);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), "null");
    }

    @Test
    public void testSetBooleanWithName() throws SQLException {

        //GIVEN
        final String parameterName = "myBoolean";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.setBoolean(parameterName, true);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), true);
    }

    @Test
    public void testSetByteWithName() throws SQLException {

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
    public void testSetShortWithName() throws SQLException {

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
    public void testSetIntWithName() throws SQLException {

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
    public void testSetLongWithName() throws SQLException {

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
    public void testSetFloatWithName() throws SQLException {

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
    public void testSetDoubleWithName() throws SQLException {

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
    public void testSetBigDecimalWithName() throws SQLException {

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
    public void testSetStringWithName() throws SQLException {

        //GIVEN
        final String parameterName = "myString";
        final JdbcCallableStatement callableStatement = generateCallableStatementWithParameter(parameterName);

        //WHEN
        callableStatement.setString(parameterName, parameterName);

        //THEN
        assertEquals(callableStatement.getParameters().get(parameterName), parameterName);
    }

    @Test
    public void testSetBytesWithName() throws SQLException {

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
    public void testSetDateWithName() throws SQLException {

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
    public void testSetTimeWithName() throws SQLException {

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
    public void testSetTimeStampWithName() throws SQLException {

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
    void testGetStringByIndex() throws SQLException {

        //WHEN
        callableStatement.getString(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getString(TEST_VALUE_INDEX);
    }

    @Test
    void testGetBooleanByIndex() throws SQLException {

        //WHEN
        callableStatement.getBoolean(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getBoolean(TEST_VALUE_INDEX);
    }

    @Test
    void testGetByteByIndex() throws SQLException {

        //WHEN
        callableStatement.getByte(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getByte(TEST_VALUE_INDEX);
    }

    @Test
    void testGetBytesByIndex() throws SQLException {

        //WHEN
        callableStatement.getBytes(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getBytes(TEST_VALUE_INDEX);
    }

    @Test
    void testGetShortByIndex() throws SQLException {

        //WHEN
        callableStatement.getShort(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getShort(TEST_VALUE_INDEX);
    }

    @Test
    void testGetIntByIndex() throws SQLException {

        //WHEN
        callableStatement.getInt(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getInt(TEST_VALUE_INDEX);
    }

    @Test
    void testGetLongByIndex() throws SQLException {

        //WHEN
        callableStatement.getLong(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getLong(TEST_VALUE_INDEX);
    }

    @Test
    void testGetFloatByIndex() throws SQLException {

        //WHEN
        callableStatement.getFloat(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getFloat(TEST_VALUE_INDEX);
    }

    @Test
    void testGetDoubleByIndex() throws SQLException {

        //WHEN
        callableStatement.getDouble(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getDouble(TEST_VALUE_INDEX);
    }

    @Test
    void testGetBigDecimalByIndexWithScale() throws Exception {

        //GIVEN
        final BigDecimal bigDecimalMock = mock(BigDecimal.class);
        when(resultSetSpy.getBigDecimal(TEST_VALUE_INDEX)).thenReturn(bigDecimalMock);

        //WHEN
        callableStatement.getBigDecimal(TEST_VALUE_INDEX, 2);

        //THEN
        //noinspection ResultOfMethodCallIgnored
        verify(bigDecimalMock).setScale(2, RoundingMode.HALF_UP);
    }

    @Test
    void testGetStringByName() throws SQLException {

        //WHEN
        callableStatement.getString(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getString(TEST_VALUE_NAME);
    }

    @Test
    void testGetBooleanByName() throws SQLException {

        //WHEN
        callableStatement.getBoolean(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getBoolean(TEST_VALUE_NAME);
    }

    @Test
    void testGetByteByName() throws SQLException {

        //WHEN
        callableStatement.getByte(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getByte(TEST_VALUE_NAME);
    }

    @Test
    void testGetShortByName() throws SQLException {

        //WHEN
        callableStatement.getShort(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getShort(TEST_VALUE_NAME);
    }

    @Test
    void testGetIntByName() throws SQLException {

        //WHEN
        callableStatement.getInt(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getInt(TEST_VALUE_NAME);
    }

    @Test
    void testGetLongByName() throws SQLException {

        //WHEN
        callableStatement.getLong(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getLong(TEST_VALUE_NAME);
    }

    @Test
    void testGetFloatByName() throws SQLException {

        //WHEN
        callableStatement.getFloat(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getFloat(TEST_VALUE_NAME);
    }

    @Test
    void testGetDoubleByName() throws SQLException {

        //WHEN
        callableStatement.getDouble(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getDouble(TEST_VALUE_NAME);
    }

    @Test
    void testGetBytesByName() throws SQLException {

        //WHEN
        callableStatement.getBytes(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getBytes(TEST_VALUE_NAME);
    }

    @Test
    void testGetObjectByName() throws SQLException {

        //WHEN
        callableStatement.getObject(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getObject(TEST_VALUE_NAME);
    }

    @Test
    void testGetBigDecimalByName() throws SQLException {

        //WHEN
        callableStatement.getBigDecimal(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getBigDecimal(TEST_VALUE_NAME);
    }

    @Test
    void testDelegateWasNull() throws SQLException {


        //WHEN
        callableStatement.wasNull();

        //THEN
        verify(resultSetSpy).wasNull();
    }

    @Test
    void testGetDateByIndex() throws SQLException {

        //WHEN
        callableStatement.getDate(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getDate(TEST_VALUE_INDEX);
    }

    @Test
    void testGetTimeByIndex() throws SQLException {

        //WHEN
        callableStatement.getTime(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getTime(TEST_VALUE_INDEX);
    }

    @Test
    void testGetTimestampByIndex() throws SQLException {

        //WHEN
        callableStatement.getTimestamp(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getTimestamp(TEST_VALUE_INDEX);
    }

    @Test
    void testGetObjectByIndex() throws SQLException {

        //WHEN
        callableStatement.getObject(TEST_VALUE_INDEX);

        //THEN
        verify(resultSetSpy).getObject(TEST_VALUE_INDEX);
    }

    @Test
    void testGetDateByName() throws SQLException {

        //WHEN
        callableStatement.getDate(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getDate(TEST_VALUE_NAME);
    }

    @Test
    void testGetTimeByName() throws SQLException {

        //WHEN
        callableStatement.getTime(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getTime(TEST_VALUE_NAME);
    }

    @Test
    void testGetTimestampByName() throws SQLException {

        //WHEN
        callableStatement.getTimestamp(TEST_VALUE_NAME);

        //THEN
        verify(resultSetSpy).getTimestamp(TEST_VALUE_NAME);
    }

    @Test
    void verifyResultSetHandling() throws SQLException {

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
    public void testToString(){
        ToStringVerifier.forClass(JdbcCallableStatement.class).verify();
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(JdbcCallableStatement.class)
                .withPrefabValues(
                        JdbcResultSet.class,
                        new JdbcResultSet(mock(DataSet.class), mock(JdbcStatement.class)),
                        new JdbcResultSet(mock(DataSet.class), mock(JdbcStatement.class)))
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    private JdbcCallableStatement generateCallableStatement() {
        final String statement = "CALL myFunction(?,?)";
        return new JdbcCallableStatement(httpClient, statement, serverUrl, jdbcConnection);
    }
    private JdbcCallableStatement generateCallableStatementWithParameter(final String parameterName) {
        final String statement = "CALL myFunction("+parameterName+",?)";
        return new JdbcCallableStatement(httpClient,statement, serverUrl, jdbcConnection);
    }

}