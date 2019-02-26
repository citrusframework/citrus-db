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

import com.consol.citrus.db.driver.exchange.DatabaseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class JdbcStatement implements Statement {

    final HttpClient httpClient;
    final String serverUrl;
    final JdbcConnection connection;

    /** List of batch statements */
    final List<String> batchStatements = new LinkedList<>();

    /** The JdbcResultSet holding the result data of the query if existent  */
    JdbcResultSet resultSet;

    /** Whether the statement has been closed */
    boolean closed;

    /** The update count of the statement */
    int updateCount;

    /**
     * Default constructor using remote client reference.
     * @param httpClient The http client to use for the db communication
     * @param serverUrl Thr url of the server
     */
    JdbcStatement(final HttpClient httpClient, final String serverUrl, final JdbcConnection connection) {
        this.httpClient = httpClient;
        this.serverUrl = serverUrl;
        this.connection = connection;
    }

    @Override
    public java.sql.ResultSet executeQuery(final String sqlQuery) throws SQLException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(RequestBuilder.post(serverUrl + "/query")
                    .setEntity(new StringEntity(sqlQuery, ContentType.create("text/plain", "UTF-8")))
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()
                    || !response.getEntity().getContentType().getValue().equals("application/json")) {
                throw new SQLException("Failed to execute query: " + sqlQuery);
            }

            final DatabaseResult databaseResult = getDatabaseResult(response);
            return new JdbcResultSet(databaseResult.getDataSet(), this);
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
                throw new SQLException(String.format("Failed to execute statement '%s' due to server error: %s %s", sql, response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()));
            }

            if (response.getEntity().getContentType().getValue().equals("application/json")) {
                final DatabaseResult databaseResult = getDatabaseResult(response);

                if(databaseResult.isDataSet()){
                    resultSet = new JdbcResultSet(databaseResult.getDataSet(), this);
                    updateCount = -1;
                    return true;
                }else{
                    resultSet = null;
                    this.updateCount = databaseResult.getAffectedRows();
                    return false;
                }
            }

            return false;
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
            closed = true;
            closeResultSet();
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
        //currently not required
    }

    @Override
    public int getMaxRows() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getMaxRows'");
    }

    @Override
    public void setMaxRows(final int max) throws SQLException {
        //currently not required
    }

    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {
        //currently not required
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getQueryTimeout'");
    }

    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        //currently not required
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
        //currently not required
    }

    @Override
    public java.sql.ResultSet getResultSet() throws SQLException {
        return resultSet;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return updateCount;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        //Multiple results in one statement are currently not supported
        return false;
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        //currently not required
    }

    @Override
    public int getFetchDirection() throws SQLException {
        throw new SQLException("Not supported JDBC statement function 'getFetchDirection'");
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {
        //currently not required
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
        verifyNotClosed();
        batchStatements.add(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        verifyNotClosed();
        batchStatements.clear();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        for (final String batchStatement : batchStatements){
            execute(batchStatement);
            arrayList.add(getUpdateCount());
        }
        return ArrayUtils.toPrimitive(arrayList.toArray(new Integer[0]));
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
        return closed;
    }

    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        //currently not required
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
        //currently not required
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

    private DatabaseResult getDatabaseResult(final HttpResponse response) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getEntity().getContent(), DatabaseResult.class);
    }

    private void closeResultSet() throws SQLException {
        if(resultSet != null){
            resultSet.close();
        }
    }

    private void verifyNotClosed() throws SQLException {
        if(isClosed()){
            throw new SQLException("The statement has already been closed");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)) return false;
        if (o.getClass().equals(JdbcPreparedStatement.class)) return false;
        if (!(o instanceof JdbcStatement)) return false;
        final JdbcStatement that = (JdbcStatement) o;
        return Objects.equals(httpClient, that.httpClient) &&
                Objects.equals(serverUrl, that.serverUrl) &&
                Objects.equals(connection, that.connection) &&
                Objects.equals(batchStatements, that.batchStatements) &&
                Objects.equals(closed, that.closed) &&
                Objects.equals(updateCount, that.updateCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpClient, serverUrl, connection, batchStatements, closed, updateCount);
    }

    @Override
    public String toString() {
        return "JdbcStatement{" +
                "httpClient=" + httpClient +
                ", serverUrl='" + serverUrl + '\'' +
                ", connection=" + connection +
                ", batchStatements=" + batchStatements +
                ", closed=" + closed +
                ", updateCount=" + updateCount +
                '}';
    }

    List<String> getBatchStatements() {
        return batchStatements;
    }
}
