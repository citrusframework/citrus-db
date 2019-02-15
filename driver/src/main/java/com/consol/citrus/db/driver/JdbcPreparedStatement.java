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

import org.apache.http.client.HttpClient;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
public class JdbcPreparedStatement extends JdbcStatement implements PreparedStatement {

    private final String preparedStatement;
    private final Map<String, Object> parameters = new TreeMap<>();

    public JdbcPreparedStatement(final HttpClient httpClient, final String preparedStatement, final String serverUrl, JdbcConnection connection) {
        super(httpClient, serverUrl, connection);
        this.preparedStatement = preparedStatement;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return super.executeQuery(composeStatement());
    }

    @Override
    public int executeUpdate() throws SQLException {
        return super.executeUpdate(composeStatement());
    }

    @Override
    public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
        setParameter(parameterIndex, null);
    }

    @Override
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setByte(final int parameterIndex, final byte x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setShort(final int parameterIndex, final short x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setInt(final int parameterIndex, final int x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setLong(final int parameterIndex, final long x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setFloat(final int parameterIndex, final float x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setDouble(final int parameterIndex, final double x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setString(final int parameterIndex, final String x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setDate(final int parameterIndex, final Date x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setTime(final int parameterIndex, final Time x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void clearParameters() throws SQLException {
        parameters.clear();
    }

    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public boolean execute() throws SQLException {
        return super.execute(composeStatement());
    }

    @Override
    public void addBatch() throws SQLException {
    }

    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setCharacterStream'");
    }

    @Override
    public void setRef(final int parameterIndex, final Ref x) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setRef'");
    }

    @Override
    public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setBlob'");
    }

    @Override
    public void setClob(final int parameterIndex, final Clob x) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setClob'");
    }

    @Override
    public void setArray(final int parameterIndex, final Array x) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setArray'");
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return resultSet.getMetaData();
    }

    @Override
    public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        setParameter(parameterIndex, x.toString());
    }

    @Override
    public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        setParameter(parameterIndex, x.getTime());
    }

    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        setParameter(parameterIndex, x.getTime());
    }

    @Override
    public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        setParameter(parameterIndex, null);
    }

    @Override
    public void setURL(final int parameterIndex, final URL x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setNString(final int parameterIndex, final String x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setNCharacterStream'");
    }

    @Override
    public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setNClob'");
    }

    @Override
    public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setClob'");
    }

    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setBlob'");
    }

    @Override
    public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setNClob'");
    }

    @Override
    public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setSQLXML'");
    }

    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setAsciiStream'");
    }

    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setBinaryStream'");
    }

    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setCharacterStream'");
    }

    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setAsciiStream'");
    }

    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setBinaryStream'");
    }

    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setCharacterStream'");
    }

    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setNCharacterStream'");
    }

    @Override
    public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setClob'");
    }

    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setBlob'");
    }

    @Override
    public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
        throw new SQLException("Not supported JDBC prepared statement function 'setNClob'");
    }

    void setParameter(final int parameterIndex, final Object value){
        setParameter(String.valueOf(parameterIndex - 1), value);
    }

    void setParameter(final String parameterName, final Object value){
        parameters.put(parameterName, value);
    }

    private String composeStatement() {
        return preparedStatement + " - (" + parameters.values().stream().map(param -> param != null ? param.toString() : "null").collect(Collectors.joining(",")) + ")";
    }

    Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof JdbcPreparedStatement)) return false;
        if (o.getClass().equals(JdbcStatement.class)) return false;
        final JdbcPreparedStatement that = (JdbcPreparedStatement) o;
        return Objects.equals(preparedStatement, that.preparedStatement) &&
                Objects.equals(parameters, that.parameters)&&
                Objects.equals(httpClient, that.httpClient) &&
                Objects.equals(serverUrl, that.serverUrl) &&
                Objects.equals(connection, that.connection) &&
                Objects.equals(resultSet, that.resultSet);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(super.hashCode(), preparedStatement, parameters);
    }

    @Override
    public String toString() {
        return "JdbcPreparedStatement{" +
                "preparedStatement='" + preparedStatement + '\'' +
                ", parameters=" + parameters +
                ", resultSet=" + resultSet +
                "} " + super.toString();
    }
}

