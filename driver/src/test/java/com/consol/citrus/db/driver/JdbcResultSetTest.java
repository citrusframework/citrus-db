package com.consol.citrus.db.driver;

import com.consol.citrus.db.driver.data.Row;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.DataSetBuilder;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.io.IOUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@PrepareForTest(ConvertUtils.class)
public class JdbcResultSetTest {

    private JdbcResultSet resultSet;
    private Row rowSpy;

    private final int TEST_VALUE_INDEX_JDBC = 2;
    //Because in JDBC, arrays start at 1
    private final int TEST_VALUE_INDEX_INTERNAL = TEST_VALUE_INDEX_JDBC-1;
    private final String TEST_VALUE_NAME = "col2";

    @Test
    public void testGetStringByIndex() throws SQLException {
        //GIVEN
        final String expectedString = "bar";
        resultSet = new JdbcResultSet(generateTestDataSet(expectedString), null);
        resultSet.next();

        //WHEN
        final String string = resultSet.getString(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(string, expectedString);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, String.class);
    }

    @Test
    void testGetStringByName() throws SQLException {

        //GIVEN
        final String expectedString = "bar";
        resultSet = new JdbcResultSet(generateTestDataSet(expectedString), null);
        resultSet.next();

        //WHEN
        final String string = resultSet.getString(TEST_VALUE_NAME);

        //THEN
        assertEquals(string, expectedString);
        verify(rowSpy).getValue(TEST_VALUE_NAME, String.class);
    }

    @Test
    void testGetFloatByIndex() throws SQLException {

        //GIVEN
        final float expectedFloat = 4.2F;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedFloat), null);
        resultSet.next();
        //WHEN
        final float aFloat = resultSet.getFloat(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aFloat, expectedFloat);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, float.class);
    }

    @Test
    void testGetFloatByName() throws SQLException {

        //GIVEN
        final float expectedFloat = 4.2F;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedFloat), null);
        resultSet.next();

        //WHEN
        final float aFloat = resultSet.getFloat(TEST_VALUE_NAME);

        //THEN
        assertEquals(aFloat, expectedFloat);
        verify(rowSpy).getValue(TEST_VALUE_NAME, float.class);
    }

    @Test
    void testGetIntByIndex() throws SQLException {

        //GIVEN
        final int expectedInt = 42;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedInt), null);
        resultSet.next();

        //WHEN
        final int anInt = resultSet.getInt(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(anInt, expectedInt);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, int.class);
    }

    @Test
    void testGetIntByName() throws SQLException {

        //GIVEN
        final int expectedInt = 42;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedInt), null);
        resultSet.next();

        //WHEN
        final int anInt = resultSet.getInt(TEST_VALUE_NAME);

        //THEN
        assertEquals(anInt, expectedInt);
        verify(rowSpy).getValue(TEST_VALUE_NAME, int.class);
    }

    @Test
    void testGetBooleanByIndex() throws SQLException {

        //GIVEN
        final boolean expectedBoolean = true;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedBoolean), null);
        resultSet.next();

        //WHEN
        final boolean aBoolean = resultSet.getBoolean(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertTrue(aBoolean);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, boolean.class);
    }

    @Test
    void testGetByteByIndex() throws SQLException {

        //GIVEN
        final byte expectedByte = 42;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedByte), null);
        resultSet.next();

        //WHEN
        final byte aByte = resultSet.getByte(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aByte, expectedByte);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, byte.class);
    }

    @Test
    void testGetShortByIndex() throws SQLException {

        //GIVEN
        final short expectedShort = 42;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedShort), null);
        resultSet.next();

        //WHEN
        final short aShort = resultSet.getShort(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aShort, expectedShort);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, short.class);
    }

    @Test
    void testGetLongByIndex() throws SQLException {

        //GIVEN
        final long expectedLong = 42L;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedLong), null);
        resultSet.next();

        //WHEN
        final long aLong = resultSet.getLong(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aLong, expectedLong);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, long.class);
    }

    @Test
    void testGetDoubleByIndex() throws SQLException {

        //GIVEN
        final double expectedDouble = 4.2;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedDouble), null);
        resultSet.next();

        //WHEN
        final double aDouble = resultSet.getDouble(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aDouble, expectedDouble);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, double.class);
    }

    @Test
    void testGetBigDecimalByIndexWithScale() throws SQLException {

        //GIVEN
        final BigDecimal expectedBigDecimal = new BigDecimal(4.257);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedBigDecimal), null);
        resultSet.next();

        //WHEN
        final BigDecimal aBigDecimal = resultSet.getBigDecimal(TEST_VALUE_INDEX_JDBC, 2);

        //THEN
        assertEquals(aBigDecimal, expectedBigDecimal.setScale(2, RoundingMode.HALF_UP));
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, BigDecimal.class);
    }

    @Test
    void testGetBytesByIndex() throws SQLException {

        //GIVEN
        final byte[] expectedBytes = "nuqneh".getBytes();
        resultSet = new JdbcResultSet(generateTestDataSet(expectedBytes), null);
        resultSet.next();

        //WHEN
        final byte[] aByte = resultSet.getBytes(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aByte, expectedBytes);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, byte[].class);
    }

    @Test
    void testGetDateByIndex() throws SQLException {

        //GIVEN
        final Date expectedDate = new Date(619912800000L);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedDate), null);
        resultSet.next();

        //WHEN
        final Date aDate = resultSet.getDate(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aDate, expectedDate);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, Date.class);
    }

    @Test
    void testGetTimeByIndex() throws SQLException {

        //GIVEN
        final Time expectedTime = new Time(619912812345L);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedTime), null);
        resultSet.next();

        //WHEN
        final Time aTime = resultSet.getTime(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aTime, expectedTime);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, Time.class);
    }

    @Test
    void testGetTimestampByIndex() throws SQLException {

        //GIVEN
        final Timestamp expectedTimestamp = new Timestamp(619912812345L);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedTimestamp), null);
        resultSet.next();

        //WHEN
        final Timestamp aTimestamp = resultSet.getTimestamp(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(aTimestamp, expectedTimestamp);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, Timestamp.class);
    }

    @Test
    void testGetAsciiStreamByIndex() throws SQLException, IOException {

        //GIVEN
        final String expectedText = "nuqneh";
        resultSet = new JdbcResultSet(generateTestDataSet(expectedText), null);
        resultSet.next();

        //WHEN
        final InputStream inputStream = resultSet.getAsciiStream(TEST_VALUE_INDEX_JDBC);

        //THEN
        final String text = IOUtils.toString(inputStream, "ISO-8859-1");
        assertEquals(text, expectedText);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, String.class);
    }

    @Test
    void testGetUnicodeStreamByIndex() throws SQLException, IOException {

        //GIVEN
        final String expectedText = "nuqneh";
        resultSet = new JdbcResultSet(generateTestDataSet(expectedText), null);
        resultSet.next();

        //WHEN
        final InputStream inputStream = resultSet.getUnicodeStream(TEST_VALUE_INDEX_JDBC);

        //THEN
        final String text = IOUtils.toString(inputStream, "UTF-8");
        assertEquals(text, expectedText);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, String.class);
    }

    @Test
    void testGetBinaryStreamByIndex() throws SQLException, IOException {

        //GIVEN
        final byte[] expectedBytes = "nuqneh".getBytes();
        resultSet = new JdbcResultSet(generateTestDataSet(expectedBytes), null);
        resultSet.next();

        //WHEN
        final InputStream inputStream = resultSet.getBinaryStream(TEST_VALUE_INDEX_JDBC);

        //THEN
        final byte[] text = IOUtils.toByteArray(inputStream);
        assertEquals(text, expectedBytes);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, byte[].class);
    }

    @Test
    void testGetObjectByIndex() throws SQLException {

        //GIVEN
        final Object expectedObject = new Timestamp(619912812345L);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedObject), null);
        resultSet.next();

        //WHEN
        final Object anObject = resultSet.getObject(TEST_VALUE_INDEX_JDBC);

        //THEN
        assertEquals(anObject, expectedObject);
    }

    @Test
    void testGetBooleanByName() throws SQLException {

        //GIVEN
        final boolean expectedBoolean = true;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedBoolean), null);
        resultSet.next();

        //WHEN
        final boolean aBoolean = resultSet.getBoolean(TEST_VALUE_NAME);

        //THEN
        assertTrue(aBoolean);
        verify(rowSpy).getValue(TEST_VALUE_NAME, boolean.class);
    }

    @Test
    void testGetByteByName() throws SQLException {

        //GIVEN
        final byte expectedByte = 42;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedByte), null);
        resultSet.next();

        //WHEN
        final byte aByte = resultSet.getByte(TEST_VALUE_NAME);

        //THEN
        assertEquals(aByte, expectedByte);
        verify(rowSpy).getValue(TEST_VALUE_NAME, byte.class);
    }

    @Test
    void testGetShortByName() throws SQLException {

        //GIVEN
        final short expectedShort = 42;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedShort), null);
        resultSet.next();

        //WHEN
        final short aShort = resultSet.getShort(TEST_VALUE_NAME);

        //THEN
        assertEquals(aShort, expectedShort);
        verify(rowSpy).getValue(TEST_VALUE_NAME, short.class);
    }

    @Test
    void testGetLongByName() throws SQLException {

        //GIVEN
        final long expectedLong = 42L;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedLong), null);
        resultSet.next();

        //WHEN
        final long aLong = resultSet.getLong(TEST_VALUE_NAME);

        //THEN
        assertEquals(aLong, expectedLong);
        verify(rowSpy).getValue(TEST_VALUE_NAME, long.class);
    }

    @Test
    void testGetDoubleByName() throws SQLException {

        //GIVEN
        final double expectedDouble = 4.2;
        resultSet = new JdbcResultSet(generateTestDataSet(expectedDouble), null);
        resultSet.next();

        //WHEN
        final double aFloat = resultSet.getDouble(TEST_VALUE_NAME);

        //THEN
        assertEquals(aFloat, expectedDouble);
        verify(rowSpy).getValue(TEST_VALUE_NAME, double.class);
    }

    @Test
    void testGetBigDecimalByNameWithScale() throws SQLException {

        //GIVEN
        final BigDecimal expectedBigDecimal = new BigDecimal(4.257);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedBigDecimal), null);
        resultSet.next();

        //WHEN
        final BigDecimal aBigDecimal = resultSet.getBigDecimal(TEST_VALUE_NAME, 2);

        //THEN
        assertEquals(aBigDecimal, expectedBigDecimal.setScale(2, RoundingMode.HALF_UP));
        verify(rowSpy).getValue(TEST_VALUE_NAME, BigDecimal.class);
    }

    @Test
    void testGetBytesByName() throws SQLException {

        //GIVEN
        final byte[] expectedBytes = "Foobar".getBytes();
        resultSet = new JdbcResultSet(generateTestDataSet(expectedBytes), null);
        resultSet.next();

        //WHEN
        final byte[] bytes = resultSet.getBytes(TEST_VALUE_NAME);

        //THEN
        assertEquals(bytes, expectedBytes);
        verify(rowSpy).getValue(TEST_VALUE_NAME, byte[].class);
    }

    @Test
    void testGetDateByName() throws SQLException {

        //GIVEN
        final Date expectedDate = new Date(619912800000L);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedDate), null);
        resultSet.next();

        //WHEN
        final Date aDate = resultSet.getDate(TEST_VALUE_NAME);

        //THEN
        assertEquals(aDate, expectedDate);
        verify(rowSpy).getValue(TEST_VALUE_NAME, Date.class);
    }

    @Test
    void testGetTimeByName() throws SQLException {

        //GIVEN
        final Time expectedTime = new Time(619912812345L);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedTime), null);
        resultSet.next();

        //WHEN
        final Time aTime = resultSet.getTime(TEST_VALUE_NAME);

        //THEN
        assertEquals(aTime, expectedTime);
        verify(rowSpy).getValue(TEST_VALUE_NAME, Time.class);
    }

    @Test
    void testGetTimestampByName() throws SQLException {

        //GIVEN
        final Timestamp expectedTimestamp = new Timestamp(619912812345L);
        resultSet = new JdbcResultSet(generateTestDataSet(expectedTimestamp), null);
        resultSet.next();

        //WHEN
        final Timestamp aTimestamp = resultSet.getTimestamp(TEST_VALUE_NAME);

        //THEN
        assertEquals(aTimestamp, expectedTimestamp);
        verify(rowSpy).getValue(TEST_VALUE_NAME, Timestamp.class);
    }

    @Test
    void testGetAsciiStreamByName() throws SQLException, IOException {

        //GIVEN
        final String expectedText = "nuqneh";
        resultSet = new JdbcResultSet(generateTestDataSet(expectedText), null);
        resultSet.next();

        //WHEN
        final InputStream inputStream = resultSet.getAsciiStream(TEST_VALUE_NAME);

        //THEN
        final String text = IOUtils.toString(inputStream, "ISO-8859-1");
        assertEquals(text, expectedText);
        verify(rowSpy).getValue(TEST_VALUE_NAME, String.class);
    }

    @Test
    void testGetUnicodeStreamByName() throws SQLException, IOException {

        //GIVEN
        final String expectedText = "nuqneh";
        resultSet = new JdbcResultSet(generateTestDataSet(expectedText), null);
        resultSet.next();

        //WHEN
        final InputStream inputStream = resultSet.getUnicodeStream(TEST_VALUE_NAME);

        //THEN
        final String text = IOUtils.toString(inputStream, "UTF-8");
        assertEquals(text, expectedText);
        verify(rowSpy).getValue(TEST_VALUE_NAME, String.class);
    }

    @Test
    void testGetBinaryStreamByName() throws SQLException, IOException {

        //GIVEN
        final byte[] expectedBytes = "nuqneh".getBytes();
        resultSet = new JdbcResultSet(generateTestDataSet(expectedBytes), null);
        resultSet.next();

        //WHEN
        final InputStream inputStream = resultSet.getBinaryStream(TEST_VALUE_NAME);

        //THEN
        final byte[] text = IOUtils.toByteArray(inputStream);
        assertEquals(text, expectedBytes);
        verify(rowSpy).getValue(TEST_VALUE_NAME, byte[].class);
    }

    @Test
    void testGetCharacterStreamByIndex() throws SQLException, IOException {

        //GIVEN
        final String expectedText = "nuqneh";
        resultSet = new JdbcResultSet(generateTestDataSet(expectedText), null);
        resultSet.next();

        //WHEN
        final Reader reader = resultSet.getCharacterStream(TEST_VALUE_INDEX_JDBC);

        //THEN
        final String text = IOUtils.toString(reader);
        assertEquals(text, expectedText);
        verify(rowSpy).getValue(TEST_VALUE_INDEX_INTERNAL, String.class);
    }

    @Test
    void testGetCharacterStreamByName() throws SQLException, IOException {

        //GIVEN
        final String expectedText = "nuqneh";
        resultSet = new JdbcResultSet(generateTestDataSet(expectedText), null);
        resultSet.next();

        //WHEN
        final Reader reader = resultSet.getCharacterStream(TEST_VALUE_NAME);

        //THEN
        final String text = IOUtils.toString(reader);
        assertEquals(text, expectedText);
        verify(rowSpy).getValue(TEST_VALUE_NAME, String.class);
    }

    @Test
    void testRowUpdated() throws SQLException {

        //GIVEN
        resultSet = new JdbcResultSet(generateTestDataSet(), null);

        //WHEN
        final boolean updated = resultSet.rowUpdated();

        //THEN
        assertTrue(updated);
    }

    @Test
    void testRowInserted() throws SQLException {

        //GIVEN
        resultSet = new JdbcResultSet(generateTestDataSet(), null);

        //WHEN
        final boolean updated = resultSet.rowInserted();

        //THEN
        assertTrue(updated);
    }

    @Test
    void testRowDeleted() throws SQLException {

        //GIVEN
        resultSet = new JdbcResultSet(generateTestDataSet(), null);

        //WHEN
        final boolean updated = resultSet.rowDeleted();

        //THEN
        assertTrue(updated);
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(JdbcResultSet.class);
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(JdbcResultSet.class);
    }

    private DataSet generateTestDataSet() throws SQLException {
        return generateTestDataSet(null);
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