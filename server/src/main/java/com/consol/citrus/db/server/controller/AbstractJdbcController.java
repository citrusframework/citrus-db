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

package com.consol.citrus.db.server.controller;

import com.consol.citrus.db.driver.exchange.DatabaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class provides a basic abstract implementation of the JdbcController interface,
 * which provides logging and delegation to the implementation of the subclasses.
 *
 * @author Christoph Deppisch
 */
public abstract class AbstractJdbcController implements JdbcController {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(JdbcController.class);

    /** Holds the information whether the controller is within a transaction */
    private boolean transactionState;

    /**
     * Subclasses must provide proper data set for SQL statement.
     * @param sql The sql statement to map to a DataSet
     * @return The data set mapped to the given query
     */
    protected abstract DatabaseResult handleQuery(String sql);

    /**
     * Subclasses must provide proper data set for SQL statement.
     * @param sql The sql statement to map to a DataSet
     * @return The data set mapped to the given query
     */
    protected abstract DatabaseResult handleExecute(String sql);

    /**
     * Subclasses must provide number of row updated by SQL statement.
     * @param sql The sql statement to map to a DataSet
     * @return The amount of rows affected by the sql
     */
    protected abstract int handleUpdate(String sql);

    @Override
    public void openConnection(final Map<String, String> properties){
        if(log.isDebugEnabled()){
            final String propertiesAsString = properties.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(" | "));

            log.debug("OPEN CONNECTION with properties: {}", propertiesAsString);
        }
    }

    @Override
    public void createStatement(){
        log.debug("CREATE STATEMENT");
    }

    @Override
    public void closeConnection(){
        log.debug("CLOSE CONNECTION");
    }

    @Override
    public void createPreparedStatement(final String sql){
        log.debug("CREATE PREPARED STATEMENT: {}", sql);
    }

    @Override
    public DatabaseResult executeQuery(final String sql){
        log.debug("EXECUTE QUERY: {}", sql);
        final DatabaseResult databaseResult = handleQuery(sql);

        logDataSet(databaseResult);

        log.debug("QUERY EXECUTION SUCCESSFUL");
        return databaseResult;
    }

    @Override
    public DatabaseResult executeStatement(final String sql){
        log.debug("EXECUTE STATEMENT: {}", sql);

        final DatabaseResult databaseResult = handleExecute(sql);

        logDataSet(databaseResult);
        log.debug("STATEMENT EXECUTION SUCCESSFUL");
        return databaseResult;
    }

    @Override
    public int executeUpdate(final String sql){
        log.debug("EXECUTE UPDATE: {}", sql);

        final int rows = handleUpdate(sql);

        log.debug("ROWS UPDATED {}", rows);
        log.debug("UPDATE EXECUTION SUCCESSFUL");
        return rows;
    }

    @Override
    public void closeStatement(){
        log.debug("CLOSE STATEMENT");
    }

    @Override
    public void setTransactionState(final boolean transactionState) {
        log.debug("RECEIVED TRANSACTION STATE CHANGE: {}", transactionState);
        this.transactionState = transactionState;
    }

    @Override
    public boolean getTransactionState() {
        log.debug("GET TRANSACTION STATE: {}", transactionState);
        return this.transactionState;
    }

    @Override
    public void commitStatements() {
        log.debug("COMMIT STATEMENTS");
    }

    @Override
    public void rollbackStatements() {
        log.debug("ROLLBACK STATEMENTS");
    }

    @Override
    public void createCallableStatement(final String sql){
        log.debug("CREATE CALLABLE STATEMENT: {}", sql);
    }

    private void logDataSet(final DatabaseResult databaseResult) {
        if (databaseResult.isDataSet()) {
            log.debug("RESULT SET with {} rows", databaseResult.getDataSet().getRows().size());
        }
    }
}
