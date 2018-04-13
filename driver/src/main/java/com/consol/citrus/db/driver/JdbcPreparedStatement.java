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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
public class JdbcPreparedStatement extends JdbcStatement implements PreparedStatement {

    private final String preparedStatement;
    private List<Object> parameters = new ArrayList<>();

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
        parameters.add(parameterIndex, null);
    }

    @Override
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setByte(final int parameterIndex, final byte x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setShort(final int parameterIndex, final short x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setInt(final int parameterIndex, final int x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setLong(final int parameterIndex, final long x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setFloat(final int parameterIndex, final float x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setDouble(final int parameterIndex, final double x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setString(final int parameterIndex, final String x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setDate(final int parameterIndex, final Date x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setTime(final int parameterIndex, final Time x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void clearParameters() throws SQLException {
        parameters.clear();
    }

    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
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
        return new JdbcResultSetMetaData(dataSet);
    }

    @Override
    public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        parameters.add(parameterIndex - 1, x.toString());
    }

    @Override
    public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        parameters.add(parameterIndex - 1, x.getTime());
    }

    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        parameters.add(parameterIndex - 1, x.getTime());
    }

    @Override
    public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        parameters.add(parameterIndex, null);
    }

    @Override
    public void setURL(final int parameterIndex, final URL x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
    }

    @Override
    public void setNString(final int parameterIndex, final String x) throws SQLException {
        parameters.add(parameterIndex - 1, x);
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
        parameters.add(parameterIndex - 1, x);
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

    private String composeStatement() {
        return preparedStatement + " - (" + parameters.stream().map(param -> param != null ? param.toString() : "null").collect(Collectors.joining(",")) + ")";
    }
}
