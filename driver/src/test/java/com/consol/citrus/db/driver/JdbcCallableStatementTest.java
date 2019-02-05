package com.consol.citrus.db.driver;

import org.apache.http.client.HttpClient;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

public class JdbcCallableStatementTest {

    private HttpClient httpClient = mock(HttpClient.class);
    private String serverUrl = "localhost";
    private JdbcConnection jdbcConnection = mock(JdbcConnection.class);

    @Test
    public void testRegisterOutParameter() throws SQLException {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement();
        final int index = 2;

        //WHEN
        callableStatement.registerOutParameter(index, Types.INTEGER);

        //THEN
        assertEquals(callableStatement.getParameters().get("1"), "?");
    }

    @Test
    public void testRegisterOutParameterWithScale() throws SQLException {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement();
        final int index = 2;

        //WHEN
        callableStatement.registerOutParameter(index, Types.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get("1"), "?");
    }

    @Test
    public void testRegisterOutParameterWithTypeName() throws SQLException {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement();
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
        final JdbcCallableStatement callableStatement = generateCallableStatement();

        //WHEN
        callableStatement.registerOutParameter(1, JDBCType.INTEGER);

        //THEN
        assertEquals(callableStatement.getParameters().get("0"), "?");
    }

    @Test
    public void testRegisterOutParameterWithSqlTypeAndScale() {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement();

        //WHEN
        callableStatement.registerOutParameter(1, JDBCType.INTEGER, 2);

        //THEN
        assertEquals(callableStatement.getParameters().get("0"), "?");
    }

    @Test
    public void testRegisterOutParameterWithSqlTypeAndTypeName() {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement();

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

    private JdbcCallableStatement generateCallableStatement() {
        final String statement = "CALL myFunction(?,?)";
        return new JdbcCallableStatement(httpClient, statement, serverUrl, jdbcConnection);
    }

    private JdbcCallableStatement generateCallableStatementWithParameter(final String parameterName) {
        final String statement = "CALL myFunction("+parameterName+",?)";
        return new JdbcCallableStatement(httpClient,statement, serverUrl, jdbcConnection);
    }
}