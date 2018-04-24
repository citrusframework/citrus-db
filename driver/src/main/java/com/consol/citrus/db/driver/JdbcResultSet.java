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

import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.data.Row;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * @author Christoph Deppisch
 */
public class JdbcResultSet implements java.sql.ResultSet {

    /** Remote ResultSet */
    private final DataSet dataSet;
    private final JdbcStatement statement;

    //The current ResultSet data row
    private Row row;

    /**
     * Constructor using remote result set.
     */
    public JdbcResultSet(DataSet dataSet, JdbcStatement statement) throws SQLException {
        this.dataSet = dataSet;
        this.statement = statement;
    }

    @Override
    public boolean next() throws SQLException {
        try {
            row = dataSet.getNextRow();
        } catch(SQLException ex) {
            throw ex;
        } catch(Exception ex) {
            return false;
        }

        return row != null;
    }

    @Override
    public void close()	throws SQLException {
        dataSet.close();
    }

    private <T> T convert(Object value, Class<T> type) throws SQLException {
        if (value == null) {
            return null;
        }

        if (type.isInstance(value)) {
            return type.cast(value);
        }

        if (String.class.isAssignableFrom(type)) {
            return (T) value.toString();
        }

        if (Byte.class.isAssignableFrom(type)) {
            return (T) Byte.valueOf(value.toString());
        }

        if (Boolean.class.isAssignableFrom(type)) {
            return (T) Boolean.valueOf(value.toString());
        }

        if (Short.class.isAssignableFrom(type)) {
            return (T) Short.valueOf(value.toString());
        }

        if (Integer.class.isAssignableFrom(type)) {
            return (T) Integer.valueOf(value.toString());
        }

        if (Long.class.isAssignableFrom(type)) {
            return (T) Long.valueOf(value.toString());
        }

        if (Double.class.isAssignableFrom(type)) {
            return (T) Double.valueOf(value.toString());
        }

        if (Float.class.isAssignableFrom(type)) {
            return (T) Float.valueOf(value.toString());
        }

        if (Timestamp.class.isAssignableFrom(type)) {
            return (T) Timestamp.valueOf(value.toString());
        }

        if (Time.class.isAssignableFrom(type)) {
            return (T) Time.valueOf(value.toString());
        }

        if (Date.class.isAssignableFrom(type)) {
            return (T) Date.valueOf(value.toString());
        }

        throw new SQLException(String.format("Missing conversion strategy for type %s", type));
    }

    public String getString(int columnIndex) throws SQLException {
        return convert(row.getValue(columnIndex-1), String.class);
    }

    public String getString(String columnName) throws SQLException {
        return convert(row.getValue(columnName), String.class);
    }

    public float getFloat(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnIndex-1), Float.class);
        }
    }

    public float getFloat(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnName), Float.class);
        }
    }

    public int getInt(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnIndex-1), Integer.class);
        }
    }

    public int getInt(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnName), Integer.class);
        }
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1) == null) {
            return false;
        } else {
            return convert(row.getValue(columnIndex-1), Boolean.class);
        }
    }

    public byte getByte(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnIndex-1), Byte.class);
        }
    }

    public short getShort(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnIndex-1), Short.class);
        }
    }

    public long getLong(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnIndex-1), Long.class);
        }
    }

    public double getDouble(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnIndex-1), Double.class);
        }
    }

    public BigDecimal getBigDecimal(int columnIndex,int scale) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getBigDecimal'");
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        return convert(row.getValue(columnIndex-1), String.class).getBytes();
    }

    public Date getDate(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        return convert(row.getValue(columnIndex-1), Date.class);
    }

    public Time getTime(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        return convert(row.getValue(columnIndex-1), Time.class);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        return convert(row.getValue(columnIndex-1), Timestamp.class);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        byte[] byteArray = convert(row.getValue(columnIndex-1), String.class).getBytes();
        return new ByteArrayInputStream(byteArray);
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        byte[] byteArray = convert(row.getValue(columnIndex-1), String.class).getBytes();
        return new ByteArrayInputStream(byteArray);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        byte[] byteArray = convert(row.getValue(columnIndex-1), String.class).getBytes();
        return new ByteArrayInputStream(byteArray);
    }

    public Object getObject(int columnIndex) throws SQLException {
        return row.getValue(columnIndex-1);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        Long bigdObj = convert(row.getValue(columnIndex-1), Long.class);
        return BigDecimal.valueOf(bigdObj);
    }

    public boolean getBoolean(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return false;
        } else {
            return convert(row.getValue(columnName), Boolean.class);
        }
    }

    public byte getByte(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnName), Byte.class);
        }
    }

    public short getShort(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnName), Short.class);
        }
    }

    public long getLong(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnName), Long.class);
        }
    }

    public double getDouble(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return 0;
        } else {
            return convert(row.getValue(columnName), Double.class);
        }
    }

    public BigDecimal getBigDecimal(String columnName,int scale) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getBigDecimal'");
    }

    public byte[] getBytes(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        } else {
            return convert(row.getValue(columnName), String.class).getBytes();
        }
    }

    public Date getDate(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        }

        return convert(row.getValue(columnName), Date.class);
    }

    public Time getTime(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        }

        return convert(row.getValue(columnName), Time.class);
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        }

        return convert(row.getValue(columnName), Timestamp.class);
    }

    public Object getObject(String columnName) throws SQLException {
        return row.getValue(columnName);
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        }

        Long bigdObj = convert(row.getValue(columnName), Long.class);
        return BigDecimal.valueOf(bigdObj);
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        }

        byte[] byteArray = convert(row.getValue(columnName), String.class).getBytes();
        return new ByteArrayInputStream(byteArray);
    }

    public InputStream getUnicodeStream(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        }

        byte[] byteArray = convert(row.getValue(columnName), String.class).getBytes();
        return new ByteArrayInputStream(byteArray);
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        }

        byte[] byteArray = convert(row.getValue(columnName), String.class).getBytes();
        return new ByteArrayInputStream(byteArray);
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getWarnings'");
    }

    public void clearWarnings() throws SQLException {
    }

    public String getCursorName() throws SQLException {
        return "";
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return new JdbcResultSetMetaData(dataSet);
    }

    public int findColumn(String columnName) throws SQLException {
        return row.getColumns().indexOf(columnName);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        if (row.getValue(columnIndex-1)==null) {
            return null;
        }

        byte[] byteArray = convert(row.getValue(columnIndex-1), String.class).getBytes();
        return new InputStreamReader(new ByteArrayInputStream(byteArray));
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        if (row.getValue(columnName)==null) {
            return null;
        }

        byte[] byteArray = convert(row.getValue(columnName), String.class).getBytes();
        return new InputStreamReader(new ByteArrayInputStream(byteArray));
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
    }

    public void afterLast() throws SQLException {
    }

    public boolean first() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'first'");
    }

    public boolean last() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'last'");
    }

    public int getRow() throws SQLException {
        return dataSet.getCursor() + 1;
    }

    public boolean absolute(int row) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'absolute'");
    }

    public boolean relative(int rows) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'relative'");
    }

    public boolean previous() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'previous'");
    }

    public void setFetchDirection(int direction) throws SQLException {
    }

    public int getFetchDirection() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getFetchDirection'");
    }

    public void setFetchSize(int rows) throws SQLException {
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
        return dataSet.getRows().size() > 0;
    }

    public boolean rowInserted() throws SQLException {
        return dataSet.getRows().size() > 0;
    }

    public boolean rowDeleted() throws SQLException {
        return dataSet.getRows().size() > 0;
    }

    public void updateNull(int columnIndex) throws SQLException {
    }

    public void updateBoolean(int columnIndex,boolean x) throws SQLException {
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
    }

    public void updateShort(int columnIndex,short x) throws SQLException {
    }

    public void updateInt(int columnIndex,int x) throws SQLException {
    }

    public void updateLong(int columnIndex,long x) throws SQLException {
    }

    public void updateFloat(int columnIndex,float x) throws SQLException {
    }

    public void updateDouble(int columnIndex,double x) throws SQLException {
    }

    public void updateBigDecimal(int columnIndex,BigDecimal x) throws SQLException {
    }

    public void updateString(int columnIndex,String x) throws SQLException {
    }

    public void updateBytes(int columnIndex,byte[] x) throws SQLException {
    }

    public void updateDate(int columnIndex,Date x) throws SQLException {
    }

    public void updateTime(int columnIndex,Time x) throws SQLException {
    }

    public void updateTimestamp(int columnIndex,Timestamp x) throws SQLException {
    }

    public void updateBinaryStream(int columnIndex,InputStream x,int length) throws SQLException {
    }

    public void updateCharacterStream(int columnIndex,Reader x,int length) throws SQLException {
    }

    public void updateObject(int columnIndex,Object x,int scale) throws SQLException {
    }

    public void updateObject(int columnIndex,Object x) throws SQLException {
    }

    public void updateNull(String columnName) throws SQLException {
    }

    public void updateByte(String columnName, byte x) throws SQLException {
    }

    public void updateShort(String columnName, short x) throws SQLException {
    }

    public void updateInt(String columnName,int x) throws SQLException {
    }

    public void updateLong(String columnName,long x) throws SQLException {
    }

    public void updateFloat(String columnName, float x) throws SQLException {
    }

    public void updateDouble(String columnName,double x) throws SQLException {
    }

    public void updateBigDecimal(String columnName,BigDecimal x) throws SQLException {
    }

    public void updateString(String columnName,String x) throws SQLException {
    }

    public void updateBytes(String columnName,byte[] x) throws SQLException {
    }

    public void updateDate(String columnName,Date x) throws SQLException {
    }

    public void updateTime(String columnName, Time x) throws SQLException {
    }

    public void updateTimestamp(String columnName,Timestamp x) throws SQLException {
    }

    public void updateAsciiStream(String columnName,InputStream x,int length) throws SQLException {
    }

    public void updateBinaryStream(String columnName,InputStream x,int length) throws SQLException {
    }

    public void updateCharacterStream(String columnName,Reader reader,int length) throws SQLException {
    }

    public void updateObject(String columnName,Object x,int scale) throws SQLException {
    }

    public void updateObject(String columnName,Object x) throws SQLException {
    }

    public void insertRow() throws SQLException {
    }

    public void updateRow()throws SQLException {
    }

    public void deleteRow()  throws SQLException {
    }

    public void refreshRow()  throws SQLException {
    }

    public void cancelRowUpdates() throws SQLException {
    }

    public void moveToInsertRow() throws SQLException {
    }

    public void moveToCurrentRow() throws SQLException {
    }

    public Statement getStatement()  throws SQLException {
        return statement;
    }


    public Date getDate(int columnIndex,Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getDate'");
    }

    public Date getDate(String columnName,Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getDate'");
    }

    public Time getTime(int columnIndex,Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getTime'");
    }

    public Time getTime(String columnName,Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getTime'");
    }

    public Timestamp getTimestamp(int columnIndex,Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getTimestamp'");
    }

    public Timestamp getTimestamp(String columnName,Calendar cal) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getTimestamp'");
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getURL'");
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getURL'");
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException  {
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException  {
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException  {
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException  {
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException  {
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException  {
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException  {
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException  {
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getRowId'");
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getRowId'");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException  {
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException  {
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getHoldability'");
    }

    @Override
    public boolean isClosed() throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'isClosed'");
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException  {
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException  {
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException  {
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException  {
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNClob'");
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNClob'");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getSQLXML'");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getSQLXML'");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException  {
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException  {
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNString'");
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNString'");
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNCharacterStream'");
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getNCharacterStream'");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException  {
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException  {
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException  {
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException  {
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException  {
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException  {
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException  {
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException  {
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException  {
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException  {
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException  {
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException  {
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException  {
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException  {
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException  {
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException  {
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException  {
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException  {
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException  {
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException  {
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException  {
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException  {
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException  {
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException  {
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException  {
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException  {
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException  {
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getObject'");
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getObject'");
    }

    public boolean wasNull()throws SQLException {
        return row.getLastValue() == null;
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException {
    }


    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
    }

    public Object getObject(int i, Map map) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getObject'");
    }

    public Ref getRef(int i) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getRef'");
    }

    public Blob getBlob(int i) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getBlob'");
    }

    public Clob getClob(int i) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getClob'");
    }

    public Array getArray(int i) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getArray'");
    }

    public Object getObject(String colName, Map map) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getObject'");
    }

    public Ref getRef(String colName) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getRef'");
    }

    public Blob getBlob(String colName) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getBlob'");
    }

    public Clob getClob(String colName) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getClob'");
    }
    public Array getArray(String colName) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'getArray'");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'unwrap'");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLException("Not supported JDBC result set function 'isWrapperFor'");
    }
}