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

import com.consol.citrus.db.driver.data.CitrusClob;
import com.consol.citrus.db.driver.utils.LobUtils;
import org.apache.http.client.HttpClient;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public final class JdbcCallableStatement extends JdbcPreparedStatement implements CallableStatement {

    private LobUtils lobUtils = new LobUtils();

    public JdbcCallableStatement(final HttpClient httpClient,
                                 final String callableStatement,
                                 final String serverUrl,
                                 final JdbcConnection connection) {
        super(httpClient, callableStatement, serverUrl, connection);
    }

    JdbcCallableStatement(final HttpClient httpClient,
                          final String callableStatement,
                          final String serverUrl,
                          final JdbcConnection connection,
                          final LobUtils lobUtils) {
        this(httpClient, callableStatement, serverUrl, connection);
        this.lobUtils = lobUtils;
    }

    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
        setOutParameter(parameterIndex);
    }

    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
        registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public boolean wasNull() throws SQLException {
        prepareResultSet();
        return resultSet.wasNull();
    }

    @Override
    public String getString(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getString(parameterIndex);
    }

    @Override
    public boolean getBoolean(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getBoolean(parameterIndex);
    }

    @Override
    public byte getByte(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getByte(parameterIndex);
    }

    @Override
    public short getShort(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getShort(parameterIndex);
    }

    @Override
    public int getInt(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getInt(parameterIndex);
    }

    @Override
    public long getLong(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getLong(parameterIndex);
    }

    @Override
    public float getFloat(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getFloat(parameterIndex);
    }

    @Override
    public double getDouble(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getDouble(parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
        return getBigDecimal(parameterIndex).setScale(scale, RoundingMode.HALF_UP);
    }

    @Override
    public byte[] getBytes(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getBytes(parameterIndex);
    }

    @Override
    public Date getDate(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getDate(parameterIndex);
    }

    @Override
    public Time getTime(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getTime(parameterIndex);
    }

    @Override
    public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getTimestamp(parameterIndex);
    }

    @Override
    public Object getObject(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getObject(parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getBigDecimal(parameterIndex);
    }

    @Override
    public Object getObject(final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        notSupported("getObject(int parameterIndex, Map<String, Class<?>> map)");
        return null;
    }

    @Override
    public Ref getRef(final int parameterIndex) throws SQLException {
        notSupported("getRef(int parameterIndex)");
        return null;
    }

    @Override
    public Blob getBlob(final int parameterIndex) throws SQLException {
        prepareResultSet();
        return resultSet.getBlob(parameterIndex);
    }

    @Override
    public Clob getClob(final int parameterIndex) {
        prepareResultSet();
        return resultSet.getClob(parameterIndex);
    }

    @Override
    public Array getArray(final int parameterIndex) throws SQLException {
        notSupported("getArray(int parameterIndex)");
        return null;
    }

    @Override
    public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
        notSupported("getDate(int parameterIndex, Calendar cal)");
        return null;
    }

    @Override
    public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
        notSupported("getTime(int parameterIndex, Calendar cal)");
        return null;
    }

    @Override
    public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
        notSupported("getTimestamp(int parameterIndex, Calendar cal)");
        return null;
    }

    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(final String parameterName, final int sqlType){
        setOutParameter(parameterName);
    }

    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final int scale){
        registerOutParameter(parameterName, sqlType);
    }

    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final String typeName){
        registerOutParameter(parameterName, sqlType);
    }

    @Override
    public URL getURL(final int parameterIndex) throws SQLException {
        notSupported("getURL(int parameterIndex)");
        return null;
    }

    @Override
    public void setURL(final String parameterName, final URL val){
        setParameter(parameterName, val);
    }

    @Override
    public void setNull(final String parameterName, final int sqlType){
        setParameter(parameterName, "null");
    }

    @Override
    public void setBoolean(final String parameterName, final boolean x){
        setParameter(parameterName, x);
    }

    @Override
    public void setByte(final String parameterName, final byte x){
        setParameter(parameterName, x);
    }

    @Override
    public void setShort(final String parameterName, final short x){
        setParameter(parameterName, x);
    }

    @Override
    public void setInt(final String parameterName, final int x){
        setParameter(parameterName, x);
    }

    @Override
    public void setLong(final String parameterName, final long x){
        setParameter(parameterName, x);
    }

    @Override
    public void setFloat(final String parameterName, final float x){
        setParameter(parameterName, x);
    }

    @Override
    public void setDouble(final String parameterName, final double x){
        setParameter(parameterName, x);
    }

    @Override
    public void setBigDecimal(final String parameterName, final BigDecimal x){
        setParameter(parameterName, x);
    }

    @Override
    public void setString(final String parameterName, final String x){
        setParameter(parameterName, x);
    }

    @Override
    public void setBytes(final String parameterName, final byte[] x){
        setParameter(parameterName, x);
    }

    @Override
    public void setDate(final String parameterName, final Date x){
        setParameter(parameterName, x);
    }

    @Override
    public void setTime(final String parameterName, final Time x){
        setParameter(parameterName, x);
    }

    @Override
    public void setTimestamp(final String parameterName, final Timestamp x) {
        setParameter(parameterName, x);
    }

    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        notSupported("setAsciiStream(String parameterName, InputStream x, int length)");
    }

    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        notSupported("setBinaryStream(String parameterName, InputStream x, int length)");
    }

    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        notSupported("setObject(String parameterName, Object x, int targetSqlType, int scale)");
    }

    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        notSupported("setObject(String parameterName, Object x, int targetSqlType)");
    }

    @Override
    public void setObject(final String parameterName, final Object x) throws SQLException {
        notSupported("setObject(String parameterName, Object x)");
    }

    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
        notSupported("setCharacterStream(String parameterName, Reader reader, int length)");
    }

    @Override
    public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
        notSupported("setDate(String parameterName, Date x, Calendar cal)");
    }

    @Override
    public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
        notSupported("setTime(String parameterName, Time x, Calendar cal)");
    }

    @Override
    public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
        notSupported("setTimestamp(String parameterName, Timestamp x, Calendar cal)");
    }

    @Override
    public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        notSupported("setNull(String parameterName, int sqlType, String typeName)");
    }

    @Override
    public String getString(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getString(parameterName);
    }

    @Override
    public boolean getBoolean(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getBoolean(parameterName);
    }

    @Override
    public byte getByte(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getByte(parameterName);
    }

    @Override
    public short getShort(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getShort(parameterName);
    }

    @Override
    public int getInt(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getInt(parameterName);
    }

    @Override
    public long getLong(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getLong(parameterName);
    }

    @Override
    public float getFloat(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getFloat(parameterName);
    }

    @Override
    public double getDouble(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getDouble(parameterName);
    }

    @Override
    public byte[] getBytes(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getBytes(parameterName);
    }

    @Override
    public Date getDate(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getDate(parameterName);
    }

    @Override
    public Time getTime(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getTime(parameterName);
    }

    @Override
    public Timestamp getTimestamp(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getTimestamp(parameterName);
    }

    @Override
    public Object getObject(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getObject(parameterName);
    }

    @Override
    public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getBigDecimal(parameterName);
    }

    @Override
    public Object getObject(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        notSupported("getObject(String parameterName, Map<String, Class<?>> map)");
        return null;
    }

    @Override
    public Ref getRef(final String parameterName) throws SQLException {
        notSupported("getRef(String parameterName)");
        return null;
    }

    @Override
    public Blob getBlob(final String parameterName) throws SQLException {
        prepareResultSet();
        return resultSet.getBlob(parameterName);
    }

    @Override
    public Clob getClob(final String parameterName) {
        prepareResultSet();
        return resultSet.getClob(parameterName);
    }

    @Override
    public Array getArray(final String parameterName) throws SQLException {
        notSupported("getArray(String parameterName)");
        return null;
    }

    @Override
    public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
        notSupported("getDate(String parameterName, Calendar cal)");
        return null;
    }

    @Override
    public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
        notSupported("getTime(String parameterName, Calendar cal)");
        return null;
    }

    @Override
    public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
        notSupported("getTimestamp(String parameterName, Calendar cal)");
        return null;
    }

    @Override
    public URL getURL(final String parameterName) throws SQLException {
        notSupported("getURL(String parameterName)");
        return null;
    }

    @Override
    public RowId getRowId(final int parameterIndex) throws SQLException {
        notSupported("getRowId(int parameterIndex)");
        return null;
    }

    @Override
    public RowId getRowId(final String parameterName) throws SQLException {
        notSupported("getRowId(String parameterName)");
        return null;
    }

    @Override
    public void setRowId(final String parameterName, final RowId x) throws SQLException {
        notSupported("setRowId(String parameterName, RowId x)");
    }

    @Override
    public void setNString(final String parameterName, final String value) throws SQLException {
        notSupported("setNString(String parameterName, String value)");
    }

    @Override
    public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
        notSupported("setNCharacterStream(String parameterName, Reader value, long length)");
    }

    @Override
    public void setNClob(final String parameterName, final NClob value) throws SQLException {
        notSupported("setNClob(String parameterName, NClob value)");
    }

    @Override
    public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        if(lobUtils.fitsInInt(length)){
            final CitrusClob clobFromReader = lobUtils.createClobFromReader(reader, (int) length);
            setParameter(parameterName, clobFromReader);
        }
    }

    @Override
    public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
        notSupported("setBlob(String parameterName, InputStream inputStream, long length)");
    }

    @Override
    public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        notSupported("setNClob(String parameterName, Reader reader, long length)");
    }

    @Override
    public NClob getNClob(final int parameterIndex) throws SQLException {
        notSupported("getNClob(int parameterIndex)");
        return null;
    }

    @Override
    public NClob getNClob(final String parameterName) throws SQLException {
        notSupported("getNClob(String parameterName)");
        return null;
    }

    @Override
    public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
        notSupported("setSQLXML(String parameterName, SQLXML xmlObject)");

    }

    @Override
    public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
        notSupported("getSQLXML(int parameterIndex)");
        return null;
    }

    @Override
    public SQLXML getSQLXML(final String parameterName) throws SQLException {
        notSupported("getSQLXML(String parameterName)");
        return null;
    }

    @Override
    public String getNString(final int parameterIndex) throws SQLException {
        notSupported("getNString(int parameterIndex)");
        return null;
    }

    @Override
    public String getNString(final String parameterName) throws SQLException {
        notSupported("getNString(String parameterName)");
        return null;
    }

    @Override
    public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
        notSupported("getNCharacterStream(int parameterIndex)");
        return null;
    }

    @Override
    public Reader getNCharacterStream(final String parameterName) throws SQLException {
        notSupported("getNCharacterStream(String parameterName)");
        return null;
    }

    @Override
    public Reader getCharacterStream(final int parameterIndex) throws SQLException {
        notSupported("getCharacterStream(int parameterIndex)");
        return null;
    }

    @Override
    public Reader getCharacterStream(final String parameterName) throws SQLException {
        notSupported("getCharacterStream(String parameterName)");
        return null;
    }

    @Override
    public void setBlob(final String parameterName, final Blob x) throws SQLException {
        notSupported("setBlob(String parameterName,  Blob x)");
    }

    @Override
    public void setClob(final String parameterName, final Clob x) {
        setParameter(parameterName, x);
    }

    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        notSupported("setAsciiStream(String parameterName,  InputStream x, long length)");
    }

    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        notSupported("setBinaryStream(String parameterName,  InputStream x, long length)");
    }

    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
        notSupported("setCharacterStream(String parameterName,  Reader reader, long length)");
    }

    @Override
    public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
        notSupported("setAsciiStream(String parameterName, InputStream x)");
    }

    @Override
    public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
        notSupported("setBinaryStream(String parameterName, InputStream x)");
    }

    @Override
    public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
        notSupported("setCharacterStream(String parameterName, Reader reader)");
    }

    @Override
    public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
        notSupported("setNCharacterStream(String parameterName, Reader value)");
    }

    @Override
    public void setClob(final String parameterName, final Reader reader) throws SQLException {
        final CitrusClob citrusClob = lobUtils.createClobFromReader(reader, -1);
        setParameter(parameterName, citrusClob);
    }

    @Override
    public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
        notSupported("setBlob(String parameterName, InputStream inputStream)");
    }

    @Override
    public void setNClob(final String parameterName, final Reader reader) throws SQLException {
        notSupported("setNClob(String parameterName, Reader reader)");
    }

    @Override
    public <T> T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
        notSupported("getObject(int parameterIndex, Class<T> type)");
        return null;
    }

    @Override
    public <T> T getObject(final String parameterName, final Class<T> type) throws SQLException {
        notSupported("getObject(String parameterName, Class<T> type)");
        return null;
    }

    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType){
        setOutParameter(parameterIndex);
    }

    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType, final int scale){
        registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType, final String typeName){
        registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType){
        setOutParameter(parameterName);
    }

    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType, final int scale){
        registerOutParameter(parameterName, sqlType);
    }

    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType, final String typeName){
        registerOutParameter(parameterName, sqlType);
    }

    private void setOutParameter(final int parameterIndex) {
        setParameter(parameterIndex, "?");
    }

    private void setOutParameter(final String parameterName) {
        setParameter(parameterName, "?");
    }

    private void prepareResultSet() {
        if(resultSet.getRow() == 0){
            resultSet.next();
        }
    }

    private void notSupported(final String featureDescription) throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("Not supported feature: " + featureDescription );
    }
}
