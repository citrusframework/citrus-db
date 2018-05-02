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
import com.consol.citrus.db.driver.json.JsonDataSetProducer;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Objects;

/**
 * @author Christoph Deppisch
 */
public class JdbcStatement implements Statement {

    private final HttpClient httpClient;
    private final String serverUrl;
    private final JdbcConnection connection;

    protected DataSet dataSet = new DataSet();

    /**
     * Default constructor using remote client reference.
     * @param httpClient The http client to use for the db communication
     * @param serverUrl Thr url of the server
     */
    JdbcStatement(final HttpClient httpClient, final String serverUrl, JdbcConnection connection) {
        this.httpClient = httpClient;
        this.serverUrl = serverUrl;
        this.connection = connection;
    }

    @Override
    public java.sql.ResultSet executeQuery(final String sqlQuery) throws SQLException {
        HttpResponse response = null;
        try {
        	//Tibco Ping-Queries abfangen
	        if (sqlQuery.equals("SELECT USER from DUAL")){
	        	return new JdbcResultSet(null, this);
	        }
            response = httpClient.execute(RequestBuilder.post(serverUrl + "/query")
                    .setEntity(new StringEntity(sqlQuery, ContentType.create("text/plain", "UTF-8")))
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()
                    || !response.getEntity().getContentType().getValue().equals("application/json")) {
                throw new SQLException("Failed to execute query: " + sqlQuery);
            }

            dataSet = new JsonDataSetProducer(response.getEntity().getContent()).produce();

            return new JdbcResultSet(dataSet, this);
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public int executeUpdate(final String sql) throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(RequestBuilder.post(serverUrl + "/update")
                    .setEntity(new StringEntity(sql, ContentType.create("text/plain", "UTF-8")))
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to execute update: " + EntityUtils.toString(response.getEntity()));
            }

            final String responseBody = EntityUtils.toString(response.getEntity());
            return Integer.valueOf(responseBody);
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public boolean execute(final String sql) throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(RequestBuilder.post(serverUrl + "/execute")
                    .setEntity(new StringEntity(sql, ContentType.create("text/plain", "UTF-8")))
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to execute statement: " + sql);
            }

            if (response.getEntity().getContentType().getValue().equals("application/json")) {
                dataSet = new JsonDataSetProducer(response.getEntity().getContent()).produce();
            }

            return true;
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
            response = httpClient.execute(RequestBuilder.delete(serverUrl + "/statement")
                    .build());

            if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
                throw new SQLException("Failed to close statement");
            }
        } catch (final IOException e) {
            throw new SQLException(e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getMaxFieldSize'");
    }

    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
    }

    @Override
    public int getMaxRows() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getMaxRows'");
    }

    @Override
    public void setMaxRows(final int max) throws SQLException {
    }

    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getQueryTimeout'");
    }

    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
    }

    @Override
    public void cancel() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'cancel'");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'clearWarnings'");
    }

    @Override
    public void setCursorName(final String name) throws SQLException {
    }

    @Override
    public java.sql.ResultSet getResultSet() throws SQLException {
        return new JdbcResultSet(dataSet, this);
    }

    @Override
    public int getUpdateCount() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getUpdateCount'");
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getMoreResults'");
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
    }

    @Override
    public int getFetchDirection() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getFetchDirection'");
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {
    }

    @Override
    public int getFetchSize() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getFetchSize'");
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getResultSetConcurrency'");
    }

    @Override
    public int getResultSetType() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getResultSetType'");
    }

    @Override
    public void addBatch(final String sql) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'addBatch'");
    }

    @Override
    public void clearBatch() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'clearBatch'");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeBatch'");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public boolean getMoreResults(final int current) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getMoreResults'");
    }

    @Override
    public java.sql.ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getGeneratedKeys'");
    }

    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeUpdate'");
    }

    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeUpdate'");
    }

    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeUpdate'");
    }

    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'execute'");
    }

    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'execute'");
    }

    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'execute'");
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getResultSetHoldability'");
    }

    @Override
    public boolean isClosed() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'isClosed'");
    }

    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
    }

    @Override
    public boolean isPoolable() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'isPoolable'");
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'closeOnCompletion'");
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'isCloseOnCompletion'");
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getLargeUpdateCount'");
    }

    @Override
    public void setLargeMaxRows(final long max) throws SQLException {
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getLargeMaxRows'");
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeLargeBatch'");
    }

    @Override
    public long executeLargeUpdate(final String sql) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeLargeUpdate'");
    }

    @Override
    public long executeLargeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeLargeUpdate'");
    }

    @Override
    public long executeLargeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeLargeUpdate'");
    }

    @Override
    public long executeLargeUpdate(final String sql, final String[] columnNames) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'executeLargeUpdate'");
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'unwrap'");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'isWrapperFor'");
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JdbcStatement that = (JdbcStatement) o;
        return Objects.equals(httpClient, that.httpClient) &&
                Objects.equals(serverUrl, that.serverUrl) &&
                Objects.equals(dataSet, that.dataSet);
    }
}