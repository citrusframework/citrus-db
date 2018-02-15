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

import org.apache.http.client.HttpClient;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class JdbcCallableStatement implements CallableStatement {


    protected final HttpClient httpClient;
    private final String serverUrl;

    /**
     * Default constructor using remote client reference.
     * @param httpClient The http client to use for the db communication
     * @param serverUrl Thr url of the server
     */
    JdbcCallableStatement(final HttpClient httpClient, final String serverUrl) {
        this.httpClient = httpClient;
        this.serverUrl = serverUrl;
    }

    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {

    }

    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {

    }

    @Override
    public boolean wasNull() throws SQLException {
        return false;
    }

    @Override
    public String getString(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public boolean getBoolean(final int parameterIndex) throws SQLException {
        return false;
    }

    @Override
    public byte getByte(final int parameterIndex) throws SQLException {
        return 0;
    }

    @Override
    public short getShort(final int parameterIndex) throws SQLException {
        return 0;
    }

    @Override
    public int getInt(final int parameterIndex) throws SQLException {
        return 0;
    }

    @Override
    public long getLong(final int parameterIndex) throws SQLException {
        return 0;
    }

    @Override
    public float getFloat(final int parameterIndex) throws SQLException {
        return 0;
    }

    @Override
    public double getDouble(final int parameterIndex) throws SQLException {
        return 0;
    }

    @Override
    public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
        return null;
    }

    @Override
    public byte[] getBytes(final int parameterIndex) throws SQLException {
        return new byte[0];
    }

    @Override
    public Date getDate(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {

    }

    @Override
    public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {

    }

    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {

    }

    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {

    }

    @Override
    public URL getURL(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public void setURL(final String parameterName, final URL val) throws SQLException {

    }

    @Override
    public void setNull(final String parameterName, final int sqlType) throws SQLException {

    }

    @Override
    public void setBoolean(final String parameterName, final boolean x) throws SQLException {

    }

    @Override
    public void setByte(final String parameterName, final byte x) throws SQLException {

    }

    @Override
    public void setShort(final String parameterName, final short x) throws SQLException {

    }

    @Override
    public void setInt(final String parameterName, final int x) throws SQLException {

    }

    @Override
    public void setLong(final String parameterName, final long x) throws SQLException {

    }

    @Override
    public void setFloat(final String parameterName, final float x) throws SQLException {

    }

    @Override
    public void setDouble(final String parameterName, final double x) throws SQLException {

    }

    @Override
    public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {

    }

    @Override
    public void setString(final String parameterName, final String x) throws SQLException {

    }

    @Override
    public void setBytes(final String parameterName, final byte[] x) throws SQLException {

    }

    @Override
    public void setDate(final String parameterName, final Date x) throws SQLException {

    }

    @Override
    public void setTime(final String parameterName, final Time x) throws SQLException {

    }

    @Override
    public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {

    }

    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {

    }

    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {

    }

    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {

    }

    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {

    }

    @Override
    public void setObject(final String parameterName, final Object x) throws SQLException {

    }

    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {

    }

    @Override
    public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {

    }

    @Override
    public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {

    }

    @Override
    public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {

    }

    @Override
    public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {

    }

    @Override
    public String getString(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public boolean getBoolean(final String parameterName) throws SQLException {
        return false;
    }

    @Override
    public byte getByte(final String parameterName) throws SQLException {
        return 0;
    }

    @Override
    public short getShort(final String parameterName) throws SQLException {
        return 0;
    }

    @Override
    public int getInt(final String parameterName) throws SQLException {
        return 0;
    }

    @Override
    public long getLong(final String parameterName) throws SQLException {
        return 0;
    }

    @Override
    public float getFloat(final String parameterName) throws SQLException {
        return 0;
    }

    @Override
    public double getDouble(final String parameterName) throws SQLException {
        return 0;
    }

    @Override
    public byte[] getBytes(final String parameterName) throws SQLException {
        return new byte[0];
    }

    @Override
    public Date getDate(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public URL getURL(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public RowId getRowId(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public RowId getRowId(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public void setRowId(final String parameterName, final RowId x) throws SQLException {

    }

    @Override
    public void setNString(final String parameterName, final String value) throws SQLException {

    }

    @Override
    public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {

    }

    @Override
    public void setNClob(final String parameterName, final NClob value) throws SQLException {

    }

    @Override
    public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {

    }

    @Override
    public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {

    }

    @Override
    public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {

    }

    @Override
    public NClob getNClob(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public NClob getNClob(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {

    }

    @Override
    public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public SQLXML getSQLXML(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public String getNString(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public String getNString(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream(final int parameterIndex) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream(final String parameterName) throws SQLException {
        return null;
    }

    @Override
    public void setBlob(final String parameterName, final Blob x) throws SQLException {

    }

    @Override
    public void setClob(final String parameterName, final Clob x) throws SQLException {

    }

    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {

    }

    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {

    }

    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {

    }

    @Override
    public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {

    }

    @Override
    public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {

    }

    @Override
    public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {

    }

    @Override
    public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {

    }

    @Override
    public void setClob(final String parameterName, final Reader reader) throws SQLException {

    }

    @Override
    public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {

    }

    @Override
    public void setNClob(final String parameterName, final Reader reader) throws SQLException {

    }

    @Override
    public <T> T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
        return null;
    }

    @Override
    public <T> T getObject(final String parameterName, final Class<T> type) throws SQLException {
        return null;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate() throws SQLException {
        return 0;
    }

    @Override
    public void setNull(final int parameterIndex, final int sqlType) throws SQLException {

    }

    @Override
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {

    }

    @Override
    public void setByte(final int parameterIndex, final byte x) throws SQLException {

    }

    @Override
    public void setShort(final int parameterIndex, final short x) throws SQLException {

    }

    @Override
    public void setInt(final int parameterIndex, final int x) throws SQLException {

    }

    @Override
    public void setLong(final int parameterIndex, final long x) throws SQLException {

    }

    @Override
    public void setFloat(final int parameterIndex, final float x) throws SQLException {

    }

    @Override
    public void setDouble(final int parameterIndex, final double x) throws SQLException {

    }

    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {

    }

    @Override
    public void setString(final int parameterIndex, final String x) throws SQLException {

    }

    @Override
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {

    }

    @Override
    public void setDate(final int parameterIndex, final Date x) throws SQLException {

    }

    @Override
    public void setTime(final int parameterIndex, final Time x) throws SQLException {

    }

    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {

    }

    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {

    }

    @Override
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {

    }

    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {

    }

    @Override
    public void clearParameters() throws SQLException {

    }

    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {

    }

    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {

    }

    @Override
    public boolean execute() throws SQLException {
        return false;
    }

    @Override
    public void addBatch() throws SQLException {

    }

    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {

    }

    @Override
    public void setRef(final int parameterIndex, final Ref x) throws SQLException {

    }

    @Override
    public void setBlob(final int parameterIndex, final Blob x) throws SQLException {

    }

    @Override
    public void setClob(final int parameterIndex, final Clob x) throws SQLException {

    }

    @Override
    public void setArray(final int parameterIndex, final Array x) throws SQLException {

    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {

    }

    @Override
    public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {

    }

    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {

    }

    @Override
    public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {

    }

    @Override
    public void setURL(final int parameterIndex, final URL x) throws SQLException {

    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {

    }

    @Override
    public void setNString(final int parameterIndex, final String value) throws SQLException {

    }

    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {

    }

    @Override
    public void setNClob(final int parameterIndex, final NClob value) throws SQLException {

    }

    @Override
    public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {

    }

    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {

    }

    @Override
    public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {

    }

    @Override
    public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {

    }

    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {

    }

    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {

    }

    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {

    }

    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {

    }

    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {

    }

    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {

    }

    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {

    }

    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {

    }

    @Override
    public void setClob(final int parameterIndex, final Reader reader) throws SQLException {

    }

    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {

    }

    @Override
    public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {

    }

    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(final String sql) throws SQLException {
        return 0;
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(final int max) throws SQLException {

    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(final int max) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {

    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(final String name) throws SQLException {

    }

    @Override
    public boolean execute(final String sql) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public void addBatch(final String sql) throws SQLException {

    }

    @Override
    public void clearBatch() throws SQLException {

    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public boolean getMoreResults(final int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(final boolean poolable) throws SQLException {

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return false;
    }
}
