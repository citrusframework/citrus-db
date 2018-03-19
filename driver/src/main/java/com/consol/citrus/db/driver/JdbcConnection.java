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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author Christoph Deppisch
 */
public class JdbcConnection implements Connection {

    /** Http remote client */
    private final HttpClient httpClient;
    private final String serverUrl;

    /**
     * Default constructor using remote connection reference.
     * @param httpClient The http client to use for the db communication
     * @param serverUrl Thr url of the server
     */
    JdbcConnection(final HttpClient httpClient, final String serverUrl) {
        this.httpClient = httpClient;
        this.serverUrl = serverUrl;
    }

    @Override
    public Statement createStatement() throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(RequestBuilder.get(serverUrl + "/statement")
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to create statement: " + EntityUtils.toString(response.getEntity()));
            }

            return new JdbcStatement(httpClient, serverUrl);
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public void close() throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(RequestBuilder.delete(serverUrl + "/connection")
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to close connection: " + EntityUtils.toString(response.getEntity()));
            }
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public String nativeSQL(final String sql) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'nativeSQL'");
    }

    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        HttpResponse response = null;
        try {
            final HttpUriRequest request = RequestBuilder
                    .post(serverUrl + "/connection/transaction")
                    .setEntity(new StringEntity(String.valueOf(!autoCommit)))
                    .build();
            response = httpClient.execute(request);

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to transmit auto commit value: " +
                        EntityUtils.toString(response.getEntity()));
            }
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(
                    RequestBuilder.get(serverUrl + "/connection/transaction")
                            .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to get auto commit value: " +
                        EntityUtils.toString(response.getEntity()));
            }
            return !Boolean.valueOf(EntityUtils.toString(response.getEntity()));
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public void commit() throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(
                    RequestBuilder.put(serverUrl + "/connection/transaction")
                            .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to commit statements: " +
                        EntityUtils.toString(response.getEntity()));
            }
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public void rollback() throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(
                    RequestBuilder.delete(serverUrl + "/connection/transaction")
                            .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to rollback database: " +
                        EntityUtils.toString(response.getEntity()));
            }
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return new JdbcDatabaseMetaData();
    }

    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'isReadOnly'");
    }

    @Override
    public void setCatalog(final String catalog) throws SQLException {
    }

    @Override
    public String getCatalog() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getCatalog'");
    }

    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getTransactionIsolation'");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getWarnings'");
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(RequestBuilder.post(serverUrl + "/preparedStatement")
                    .setEntity(new StringEntity(sql))
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to create prepared statement: " + EntityUtils.toString(response.getEntity()));
            }

            return new JdbcPreparedStatement(httpClient, sql, serverUrl);
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(RequestBuilder.post(serverUrl + "/callableStatement")
                    .setEntity(new StringEntity(sql))
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to create prepare call: " + EntityUtils.toString(response.getEntity()));
            }

            return new JdbcCallableStatement(httpClient, sql, serverUrl);
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'prepareCall'");
    }

    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return createStatement();
    }

    @Override
    public void setTypeMap(final Map<String,Class<?>> map) throws SQLException {
    }

    @Override
    public Map<String,Class<?>> getTypeMap() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getTypeMap'");
    }

    @Override
    public void setHoldability(final int holdability) throws SQLException {
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getHoldability'");
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'setSavepoint'");
    }

    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'setSavepoint'");
    }

    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
    }

    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
    }

    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'prepareCall'");
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public Clob createClob() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'createClob'");
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'createBlob'");
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'createNClob'");
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'createSQLXML'");
    }

    @Override
    public boolean isValid(final int timeout) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'isValid'");
    }

    @Override
    public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
    }

    @Override
    public void setClientInfo(final Properties properties) throws SQLClientInfoException {
    }

    @Override
    public String getClientInfo(final String name) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getClientInfo'");
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getClientInfo'");
    }

    @Override
    public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'createArrayOf'");
    }

    @Override
    public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'createStruct'");
    }

    @Override
    public void setSchema(final String schema) throws SQLException {
    }

    @Override
    public String getSchema() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getSchema'");
    }

    @Override
    public void abort(final Executor executor) throws SQLException {
    }

    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'getNetworkTimeout'");
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'unwrap'");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        throw new SQLException("Not supported JDBC connection function 'isWrapperFor'");
    }
}