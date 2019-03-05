/*
 * Copyright 2006-2017 the original author or authors.
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

import com.consol.citrus.db.driver.data.Row;
import com.consol.citrus.db.driver.dataset.DataSet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;


public class JdbcResultSet implements java.sql.ResultSet {

    /** Remote ResultSet */
    private final DataSet dataSet;
    private final JdbcStatement statement;

    //The current ResultSet data row
    private Row row;

    /** Indicates that this data set is closed */
    private boolean closed = false;

    /**
     * Constructor using remote result set.
     */
    public JdbcResultSet(final DataSet dataSet, final JdbcStatement statement) {
        this.dataSet = dataSet;
        this.statement = statement;
    }

    @Override
    public boolean next() throws SQLException {
        row = dataSet.getNextRow();
        return row != null;
    }

    @Override
    public void close()	throws SQLException {
        closed = true;
    }

    public String getString(final int columnIndex) throws SQLException {
        return (String) row.getValue(columnIndex-1, String.class);
    }

    public String getString(final String columnName) throws SQLException {
        return (String) row.getValue(columnName, String.class);
    }

    public float getFloat(final int columnIndex) throws SQLException {
        return (float) row.getValue(columnIndex-1, float.class);
    }

    public float getFloat(final String columnName) throws SQLException {
        return (float) row.getValue(columnName, float.class);
    }

    public int getInt(final int columnIndex) throws SQLException {
        return (int) row.getValue(columnIndex-1, int.class);
    }

    public int getInt(final String columnName) throws SQLException {
        return (int) row.getValue(columnName, int.class);
    }

    public boolean getBoolean(final int columnIndex) throws SQLException {
        return (boolean) row.getValue(columnIndex-1, boolean.class);
    }

    public byte getByte(final int columnIndex) throws SQLException {
        return (byte) row.getValue(columnIndex-1, byte.class);
    }

    public short getShort(final int columnIndex) throws SQLException {
        return (short) row.getValue(columnIndex-1, short.class);
    }

    public long getLong(final int columnIndex) throws SQLException {
        return (long) row.getValue(columnIndex-1, long.class);
    }

    public double getDouble(final int columnIndex) throws SQLException {
        return (double) row.getValue(columnIndex-1, double.class);
    }

    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        return getBigDecimal(columnIndex).setScale(scale, RoundingMode.HALF_UP);
    }

    public byte[] getBytes(final int columnIndex) throws SQLException {
        return (byte[]) row.getValue(columnIndex-1, byte[].class);
    }

    public Date getDate(final int columnIndex) throws SQLException {
        return (Date) row.getValue(columnIndex-1, Date.class);
    }

    public Time getTime(final int columnIndex) throws SQLException {
        return (Time) row.getValue(columnIndex-1, Time.class);
    }

    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        return (Timestamp) row.getValue(columnIndex-1, Timestamp.class);
    }

    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        return new ByteArrayInputStream(getString(columnIndex).getBytes());
    }

    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        return new ByteArrayInputStream(getString(columnIndex).getBytes());
    }

    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        return new ByteArrayInputStream(getBytes(columnIndex));
    }

    public Object getObject(final int columnIndex) throws SQLException {
        return row.getValue(columnIndex-1);
    }

    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        return (BigDecimal) row.getValue(columnIndex-1, BigDecimal.class);
    }

    public boolean getBoolean(final String columnName) throws SQLException {
        return (boolean) row.getValue(columnName, boolean.class);
    }

    public byte getByte(final String columnName) throws SQLException {
        return (byte) row.getValue(columnName, byte.class);
    }

    public short getShort(final String columnName) throws SQLException {
        return (short) row.getValue(columnName, short.class);
    }

    public long getLong(final String columnName) throws SQLException {
        return (long) row.getValue(columnName, long.class);
    }

    public double getDouble(final String columnName) throws SQLException {
        return (double) row.getValue(columnName, double.class);
    }

    public BigDecimal getBigDecimal(final String columnName, final int scale) throws SQLException {
        return getBigDecimal(columnName).setScale(scale, RoundingMode.HALF_UP);
    }

    public byte[] getBytes(final String columnName) throws SQLException {
        return (byte[]) row.getValue(columnName, byte[].class);
    }

    public Date getDate(final String columnName) throws SQLException {
        return (Date) row.getValue(columnName, Date.class);
    }

    public Time getTime(final String columnName) throws SQLException {
        return (Time) row.getValue(columnName, Time.class);
    }

    public Timestamp getTimestamp(final String columnName) throws SQLException {
        return (Timestamp) row.getValue(columnName, Timestamp.class);
    }

    public Object getObject(final String columnName) throws SQLException {
        return row.getValue(columnName);
    }

    public BigDecimal getBigDecimal(final String columnName) throws SQLException {
        return (BigDecimal) row.getValue(columnName, BigDecimal.class);
    }

    public InputStream getAsciiStream(final String columnName) throws SQLException {
        return new ByteArrayInputStream(getString(columnName).getBytes());
    }

    public InputStream getUnicodeStream(final String columnName) throws SQLException {
        return new ByteArrayInputStream(getString(columnName).getBytes());
    }

    public InputStream getBinaryStream(final String columnName) throws SQLException {
        return new ByteArrayInputStream(getBytes(columnName));
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getWarnings'");
    }

    public void clearWarnings() throws SQLException {
        //currently not required
    }

    public String getCursorName() throws SQLException {
        return "";
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return new JdbcResultSetMetaData(dataSet);
    }

    public int findColumn(final String columnName) throws SQLException {
        return row.getColumns().indexOf(columnName)+1;
    }

    public Reader getCharacterStream(final int columnIndex) throws SQLException {
        return new InputStreamReader(new ByteArrayInputStream(getString(columnIndex).getBytes()));
    }

    public Reader getCharacterStream(final String columnName) throws SQLException {
        return new InputStreamReader(new ByteArrayInputStream(getString(columnName).getBytes()));
    }

    public boolean isBeforeFirst() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'isBeforeFirst'");
    }

    public boolean isAfterLast() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'isAfterLast'");
    }

    public boolean isFirst() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'isFirst'");
    }

    public boolean isLast() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'isLast'");
    }

    public void beforeFirst() throws SQLException {
        //currently not required
    }

    public void afterLast() throws SQLException {
        //currently not required
    }

    public boolean first() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'first'");
    }

    public boolean last() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'last'");
    }

    public int getRow() throws SQLException {
        return dataSet.getCursor();
    }

    public boolean absolute(final int row) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'absolute'");
    }

    public boolean relative(final int rows) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'relative'");
    }

    public boolean previous() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'previous'");
    }

    public void setFetchDirection(final int direction) throws SQLException {
        //currently not required
    }

    public int getFetchDirection() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getFetchDirection'");
    }

    public void setFetchSize(final int rows) throws SQLException {
        //currently not required
    }

    public int getFetchSize() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getFetchSize'");
    }

    public int getType() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getType'");
    }

    public int getConcurrency() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getConcurrency'");
    }

    public boolean rowUpdated() throws SQLException {
        return rowModified();
    }

    public boolean rowInserted() throws SQLException {
        return rowModified();
    }

    public boolean rowDeleted() throws SQLException {
        return rowModified();
    }

    public void updateNull(final int columnIndex) throws SQLException {
        //currently not required
    }

    public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        //currently not required
    }

    public void updateByte(final int columnIndex, final byte x) throws SQLException {
        //currently not required
    }

    public void updateShort(final int columnIndex, final short x) throws SQLException {
        //currently not required
    }

    public void updateInt(final int columnIndex, final int x) throws SQLException {
        //currently not required
    }

    public void updateLong(final int columnIndex, final long x) throws SQLException {
        //currently not required
    }

    public void updateFloat(final int columnIndex, final float x) throws SQLException {
        //currently not required
    }

    public void updateDouble(final int columnIndex, final double x) throws SQLException {
        //currently not required
    }

    public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        //currently not required
    }

    public void updateString(final int columnIndex, final String x) throws SQLException {
        //currently not required
    }

    public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        //currently not required
    }

    public void updateDate(final int columnIndex, final Date x) throws SQLException {
        //currently not required
    }

    public void updateTime(final int columnIndex, final Time x) throws SQLException {
        //currently not required
    }

    public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        //currently not required
    }

    public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        //currently not required
    }

    public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        //currently not required
    }

    public void updateObject(final int columnIndex, final Object x, final int scale) throws SQLException {
        //currently not required
    }

    public void updateObject(final int columnIndex, final Object x) throws SQLException {
        //currently not required
    }

    public void updateNull(final String columnName) throws SQLException {
        //currently not required
    }

    public void updateByte(final String columnName, final byte x) throws SQLException {
        //currently not required
    }

    public void updateShort(final String columnName, final short x) throws SQLException {
        //currently not required
    }

    public void updateInt(final String columnName, final int x) throws SQLException {
        //currently not required
    }

    public void updateLong(final String columnName, final long x) throws SQLException {
        //currently not required
    }

    public void updateFloat(final String columnName, final float x) throws SQLException {
        //currently not required
    }

    public void updateDouble(final String columnName, final double x) throws SQLException {
        //currently not required
    }

    public void updateBigDecimal(final String columnName, final BigDecimal x) throws SQLException {
        //currently not required
    }

    public void updateString(final String columnName, final String x) throws SQLException {
        //currently not required
    }

    public void updateBytes(final String columnName, final byte[] x) throws SQLException {
        //currently not required
    }

    public void updateDate(final String columnName, final Date x) throws SQLException {
        //currently not required
    }

    public void updateTime(final String columnName, final Time x) throws SQLException {
        //currently not required
    }

    public void updateTimestamp(final String columnName, final Timestamp x) throws SQLException {
        //currently not required
    }

    public void updateAsciiStream(final String columnName, final InputStream x, final int length) throws SQLException {
        //currently not required
    }

    public void updateBinaryStream(final String columnName, final InputStream x, final int length) throws SQLException {
        //currently not required
    }

    public void updateCharacterStream(final String columnName, final Reader reader, final int length) throws SQLException {
        //currently not required
    }

    public void updateObject(final String columnName, final Object x, final int scale) throws SQLException {
        //currently not required
    }

    public void updateObject(final String columnName, final Object x) throws SQLException {
        //currently not required
    }

    public void insertRow() throws SQLException {
        //currently not required
    }

    public void updateRow()throws SQLException {
        //currently not required
    }

    public void deleteRow()  throws SQLException {
        //currently not required
    }

    public void refreshRow()  throws SQLException {
        //currently not required
    }

    public void cancelRowUpdates() throws SQLException {
        //currently not required
    }

    public void moveToInsertRow() throws SQLException {
        //currently not required
    }

    public void moveToCurrentRow() throws SQLException {
        //currently not required
    }

    public Statement getStatement()  throws SQLException {
        return statement;
    }


    public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getDate'");
    }

    public Date getDate(final String columnName, final Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getDate'");
    }

    public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getTime'");
    }

    public Time getTime(final String columnName, final Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getTime'");
    }

    public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getTimestamp'");
    }

    public Timestamp getTimestamp(final String columnName, final Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getTimestamp'");
    }

    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getURL'");
    }

    @Override
    public URL getURL(final String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getURL'");
    }

    @Override
    public void updateRef(final int columnIndex, final Ref x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateRef(final String columnLabel, final Ref x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBlob(final int columnIndex, final Blob x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBlob(final String columnLabel, final Blob x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateClob(final int columnIndex, final Clob x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateClob(final String columnLabel, final Clob x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateArray(final int columnIndex, final Array x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateArray(final String columnLabel, final Array x) throws SQLException  {
        //currently not required
    }

    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getRowId'");
    }

    @Override
    public RowId getRowId(final String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getRowId'");
    }

    @Override
    public void updateRowId(final int columnIndex, final RowId x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateRowId(final String columnLabel, final RowId x) throws SQLException  {
        //currently not required
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getHoldability'");
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public void updateNString(final int columnIndex, final String nString) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNString(final String columnLabel, final String nString) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException  {
        //currently not required
    }

    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNClob'");
    }

    @Override
    public NClob getNClob(final String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNClob'");
    }

    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getSQLXML'");
    }

    @Override
    public SQLXML getSQLXML(final String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getSQLXML'");
    }

    @Override
    public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException  {
        //currently not required
    }

    @Override
    public String getNString(final int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNString'");
    }

    @Override
    public String getNString(final String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNString'");
    }

    @Override
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNCharacterStream'");
    }

    @Override
    public Reader getNCharacterStream(final String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNCharacterStream'");
    }

    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateClob(final int columnIndex, final Reader reader) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateClob(final String columnLabel, final Reader reader) throws SQLException  {
        //currently not required
    }

    @Override
    public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
        //currently not required
    }

    @Override
    public void updateNClob(final String columnLabel, final Reader reader) throws SQLException  {
        //currently not required
    }

    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getObject(int, Class<T>)'");
    }

    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        throw new SQLException("Not supported JDBC result set function '(String, Class<T>)'");
    }

    public boolean wasNull()throws SQLException {
        return row.getLastValue() == null;
    }

    public void updateBoolean(final String columnName, final boolean x) throws SQLException {
        //currently not required
    }


    public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        //currently not required
    }

    public Object getObject(final int i, final Map map) throws SQLException {
        throw new SQLException("Not supported JDBC result set function '(int, Map)'");
    }

    public Ref getRef(final int i) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getRef'");
    }

    public Blob getBlob(final int i) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getBlob'");
    }

    public Clob getClob(final int i) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getClob'");
    }

    public Array getArray(final int i) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getArray'");
    }

    public Object getObject(final String colName, final Map map) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getObject'");
    }

    public Ref getRef(final String colName) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getRef'");
    }

    public Blob getBlob(final String colName) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getBlob'");
    }

    public Clob getClob(final String colName) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getClob'");
    }
    public Array getArray(final String colName) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getArray'");
    }

    private boolean rowModified() {
        return !dataSet.getRows().isEmpty();
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'unwrap'");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'isWrapperFor'");
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof JdbcResultSet)) return false;
        final JdbcResultSet resultSet = (JdbcResultSet) o;
        return closed == resultSet.closed &&
                Objects.equals(dataSet, resultSet.dataSet) &&
                Objects.equals(statement, resultSet.statement) &&
                Objects.equals(row, resultSet.row);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(dataSet, statement, row, closed);
    }

    @Override
    public String toString() {
        return "JdbcResultSet{" +
                "dataSet=" + dataSet +
                ", statement=" + statement +
                ", row=" + row +
                ", closed=" + closed +
                '}';
    }
}