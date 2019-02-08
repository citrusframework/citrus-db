package com.consol.citrus.db.driver;

import com.consol.citrus.db.driver.data.Row;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.DataSetBuilder;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
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
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class JdbcCallableStatementTest{

    private HttpClient httpClient;
    private String serverUrl = "localhost";
    private JdbcConnection jdbcConnection;
    private final int TEST_VALUE_INDEX_JDBC = 2;
    //Because in JDBC, arrays start at 1
    private final int TEST_VALUE_INDEX_INTERNAL = TEST_VALUE_INDEX_JDBC -1;
    private final String TEST_VALUE_NAME = "col2";
    private final JdbcCallableStatement callableStatement = generateCallableStatement();

    private Row rowSpy;

    @BeforeMethod
    public void setup(){
        httpClient = mock(HttpClient.class);
        jdbcConnection = mock(JdbcConnection.class);
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
    void testGetTwoValuesByIndex() throws SQLException {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement("bar");

        //WHEN
        final String firstOutParameter = callableStatement.getString(1);
        final String secondOutParameter = callableStatement.getString(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(firstOutParameter, "dummyValue");
        assertEquals(secondOutParameter, "bar");
    }

    @Test
    void testGetStringByIndex() throws SQLException {

        //GIVEN
        final String expectedString = "bar";
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedString);

        //WHEN
        final String string = callableStatement.getString(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(string, expectedString);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, String.class);
    }

    @Test
    void testGetBooleanByIndex() throws SQLException {

        //GIVEN
        final boolean expectedBoolean = true;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedBoolean);

        //WHEN
        final boolean aBoolean = callableStatement.getBoolean(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertTrue(aBoolean);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, boolean.class);
    }

    @Test
    void testGetByteByIndex() throws SQLException {

        //GIVEN
        final byte expectedByte = 42;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedByte);

        //WHEN
        final byte aByte = callableStatement.getByte(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aByte, expectedByte);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, byte.class);
    }

    @Test
    void testGetBytesByIndex() throws SQLException {

        //GIVEN
        final byte[] expectedBytes = "nuqneh".getBytes();
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedBytes);

        //WHEN
        final byte[] aByte = callableStatement.getBytes(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aByte, expectedBytes);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, byte[].class);
    }

    @Test
    void testGetShortByIndex() throws SQLException {

        //GIVEN
        final short expectedShort = 42;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedShort);

        //WHEN
        final short aShort = callableStatement.getShort(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aShort, expectedShort);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, short.class);
    }

    @Test
    void testGetIntByIndex() throws SQLException {

        //GIVEN
        final int expectedInt = 42;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedInt);

        //WHEN
        final int anInt = callableStatement.getInt(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(anInt, expectedInt);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, int.class);
    }

    @Test
    void testGetLongByIndex() throws SQLException {

        //GIVEN
        final long expectedLong = 42L;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedLong);

        //WHEN
        final long aLong = callableStatement.getLong(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aLong, expectedLong);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, long.class);
    }

    @Test
    void testGetFloatByIndex() throws SQLException {

        //GIVEN
        final float expectedFloat = 4.2F;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedFloat);

        //WHEN
        final float aFloat = callableStatement.getFloat(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aFloat, expectedFloat);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, float.class);
    }

    @Test
    void testGetDoubleByIndex() throws SQLException {

        //GIVEN
        final double expectedDouble = 4.2;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedDouble);

        //WHEN
        final double aDouble = callableStatement.getDouble(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aDouble, expectedDouble);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, double.class);
    }

    @Test
    void testGetBigDecimalByIndexWithScale() throws SQLException {

        //GIVEN
        final BigDecimal expectedBigDecimal = new BigDecimal(4.257);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedBigDecimal);

        //WHEN
        final BigDecimal aBigDecimal = callableStatement.getBigDecimal(TEST_VALUE_INDEX_JDBC, 2);

        //THEN
        assertEquals(aBigDecimal, expectedBigDecimal.setScale(2, RoundingMode.HALF_UP));
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, BigDecimal.class);
    }

    @Test
    void testGetStringByName() throws SQLException {

        //GIVEN
        final String expectedString = "bar";
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedString);

        //WHEN
        final String string = callableStatement.getString(TEST_VALUE_NAME);

        //THEN
        assertEquals(string, expectedString);
        verify(rowSpy).getValue(TEST_VALUE_NAME, String.class);
    }

    @Test
    void testGetBooleanByName() throws SQLException {

        //GIVEN
        final boolean expectedBoolean = true;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedBoolean);

        //WHEN
        final boolean aBoolean = callableStatement.getBoolean(TEST_VALUE_NAME);

        //THEN
        assertTrue(aBoolean);
        verify(rowSpy).getValue(TEST_VALUE_NAME, boolean.class);
    }

    @Test
    void testGetByteByName() throws SQLException {

        //GIVEN
        final byte expectedByte = 42;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedByte);

        //WHEN
        final byte aByte = callableStatement.getByte(TEST_VALUE_NAME);

        //THEN
        assertEquals(aByte, expectedByte);
        verify(rowSpy).getValue(TEST_VALUE_NAME, byte.class);
    }

    @Test
    void testGetShortByName() throws SQLException {

        //GIVEN
        final short expectedShort = 42;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedShort);

        //WHEN
        final short aShort = callableStatement.getShort(TEST_VALUE_NAME);

        //THEN
        assertEquals(aShort, expectedShort);
        verify(rowSpy).getValue(TEST_VALUE_NAME, short.class);
    }

    @Test
    void testGetIntByName() throws SQLException {

        //GIVEN
        final int expectedInt = 42;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedInt);

        //WHEN
        final int anInt = callableStatement.getInt(TEST_VALUE_NAME);

        //THEN
        assertEquals(anInt, expectedInt);
        verify(rowSpy).getValue(TEST_VALUE_NAME, int.class);
    }

    @Test
    void testGetLongByName() throws SQLException {

        //GIVEN
        final long expectedLong = 42L;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedLong);

        //WHEN
        final long aLong = callableStatement.getLong(TEST_VALUE_NAME);

        //THEN
        assertEquals(aLong, expectedLong);
        verify(rowSpy).getValue(TEST_VALUE_NAME, long.class);
    }

    @Test
    void testGetFloatByName() throws SQLException {

        //GIVEN
        final float expectedFloat = 4.2F;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedFloat);

        //WHEN
        final float aFloat = callableStatement.getFloat(TEST_VALUE_NAME);

        //THEN
        assertEquals(aFloat, expectedFloat);
        verify(rowSpy).getValue(TEST_VALUE_NAME, float.class);
    }

    @Test
    void testGetDoubleByName() throws SQLException {

        //GIVEN
        final double expectedDouble = 4.2;
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedDouble);

        //WHEN
        final double aFloat = callableStatement.getDouble(TEST_VALUE_NAME);

        //THEN
        assertEquals(aFloat, expectedDouble);
        verify(rowSpy).getValue(TEST_VALUE_NAME, double.class);
    }

    @Test
    void testGetBytesByName() throws SQLException {

        //GIVEN
        final byte[] expectedBytes = "Foobar".getBytes();
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedBytes);

        //WHEN
        final byte[] bytes = callableStatement.getBytes(TEST_VALUE_NAME);

        //THEN
        assertEquals(bytes, expectedBytes);
        verify(rowSpy).getValue(TEST_VALUE_NAME, byte[].class);
    }

    @Test
    void testGetObjectByName() throws SQLException {

        //GIVEN
        final Object expectedObject = "Foobar".getBytes();
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedObject);

        //WHEN
        final Object object = callableStatement.getObject(TEST_VALUE_NAME);

        //THEN
        assertEquals(object, expectedObject);
    }

    @Test
    void testGetBigDecimalByName() throws SQLException {

        //GIVEN
        final BigDecimal expectedBigDecimal = new BigDecimal(4.257);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedBigDecimal);

        //WHEN
        final BigDecimal aBigDecimal = callableStatement.getBigDecimal(TEST_VALUE_NAME);

        //THEN
        assertEquals(aBigDecimal, expectedBigDecimal);
        verify(rowSpy).getValue(TEST_VALUE_NAME, BigDecimal.class);
    }

    @Test
    void testWasNullReturnsFalseAsDefault() throws SQLException {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement();

        //WHEN
        final boolean wasNull = callableStatement.wasNull();

        //THEN
        assertFalse(wasNull);
    }

    @Test
    void testWasNullIsFalse() throws SQLException {

        //GIVEN
        final BigDecimal expectedBigDecimal = new BigDecimal(4.257);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedBigDecimal);

        //WHEN
        callableStatement.getBigDecimal(TEST_VALUE_NAME);

        //THEN
        assertFalse(callableStatement.wasNull());
    }

    @Test
    void testWasNullIsTrue() throws SQLException {

        //GIVEN
        final JdbcCallableStatement callableStatement = generateCallableStatement(null);

        //WHEN
        callableStatement.getBigDecimal(TEST_VALUE_NAME);

        //THEN
        assertTrue(callableStatement.wasNull());
    }

    @Test
    void testGetDateByIndex() throws SQLException {

        //GIVEN
        final Date expectedDate = new Date(619912800000L);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedDate);

        //WHEN
        final Date aDate = callableStatement.getDate(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aDate, expectedDate);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, Date.class);
    }

    @Test
    void testGetTimeByIndex() throws SQLException {

        //GIVEN
        final Time expectedTime = new Time(619912812345L);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedTime);

        //WHEN
        final Time aTime = callableStatement.getTime(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aTime, expectedTime);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, Time.class);
    }

    @Test
    void testGetTimestampByIndex() throws SQLException {

        //GIVEN
        final Timestamp expectedTimestamp = new Timestamp(619912812345L);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedTimestamp);

        //WHEN
        final Timestamp aTimestamp = callableStatement.getTimestamp(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aTimestamp, expectedTimestamp);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, Timestamp.class);
    }

    @Test
    void testGetObjectByIndex() throws SQLException {

        //GIVEN
        final Object expectedObject = new Timestamp(619912812345L);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedObject);

        //WHEN
        final Object anObject = callableStatement.getObject(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(anObject, expectedObject);
    }

    @Test
    void testGetDateByName() throws SQLException {

        //GIVEN
        final Date expectedDate = new Date(619912800000L);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedDate);

        //WHEN
        final Date aDate = callableStatement.getDate(TEST_VALUE_NAME);

        //THEN
        assertEquals(aDate, expectedDate);
        verify(rowSpy).getValue(TEST_VALUE_NAME, Date.class);
    }

    @Test
    void testGetTimeByName() throws SQLException {

        //GIVEN
        final Time expectedTime = new Time(619912812345L);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedTime);

        //WHEN
        final Time aTime = callableStatement.getTime(TEST_VALUE_NAME);

        //THEN
        assertEquals(aTime, expectedTime);
        verify(rowSpy).getValue(TEST_VALUE_NAME, Time.class);
    }

    @Test
    void testGetTimestampByName() throws SQLException {

        //GIVEN
        final Timestamp expectedTimestamp = new Timestamp(619912812345L);
        final JdbcCallableStatement callableStatement = generateCallableStatement(expectedTimestamp);

        //WHEN
        final Timestamp aTimestamp = callableStatement.getTimestamp(TEST_VALUE_NAME);

        //THEN
        assertEquals(aTimestamp, expectedTimestamp);
        verify(rowSpy).getValue(TEST_VALUE_NAME, Timestamp.class);
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(JdbcCallableStatement.class);
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(JdbcCallableStatement.class);
    }

    private JdbcCallableStatement generateCallableStatement() {
        final String statement = "CALL myFunction(?,?)";
        return new JdbcCallableStatement(httpClient, statement, serverUrl, jdbcConnection);
    }

    private JdbcCallableStatement generateCallableStatement(final Object testValue) throws SQLException {
        final JdbcCallableStatement callableStatement = generateCallableStatement();
        callableStatement.dataSet = generateTestDataSet(testValue);
        return callableStatement;
    }

    private JdbcCallableStatement generateCallableStatementWithParameter(final String parameterName) {
        final String statement = "CALL myFunction("+parameterName+",?)";
        return new JdbcCallableStatement(httpClient,statement, serverUrl, jdbcConnection);
    }

    private DataSet generateTestDataSet(final Object testValue) throws SQLException {
        final SortedMap<String, Object> testData = new TreeMap<>();
        testData.put("col1", "dummyValue");
        testData.put("col2", testValue);

        rowSpy = spy(new Row());
        rowSpy.setValues(testData);
        return new DataSetBuilder().add(rowSpy).build();
    }
}