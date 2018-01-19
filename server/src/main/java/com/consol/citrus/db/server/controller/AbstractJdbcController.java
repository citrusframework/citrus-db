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

import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.server.JdbcServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
public abstract class AbstractJdbcController implements JdbcController {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(JdbcController.class);

    /**
     * Subclasses must provide proper dataset for SQL statement.
     * @param sql
     * @return
     */
    protected abstract DataSet handleQuery(String sql) throws JdbcServerException;

    /**
     * Subclasses must provide number of row updated by SQL statement.
     * @param sql
     * @return
     */
    protected abstract int handleUpdate(String sql) throws JdbcServerException;

    @Override
    public void openConnection(Map<String, String> properties) throws JdbcServerException {
        log.info("OPEN CONNECTION with properties: " + properties.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(" | ")));
    }

    @Override
    public void createStatement() throws JdbcServerException {
        log.info("CREATE STATEMENT");
    }

    @Override
    public void closeConnection() throws JdbcServerException {
        log.info("CLOSE CONNECTION");
    }

    @Override
    public void createPreparedStatement(String sql) throws JdbcServerException {
        log.info("CREATE PREPARED STATEMENT: " + sql);
    }

    @Override
    public DataSet executeQuery(String sql) throws JdbcServerException {
        log.info("EXECUTE QUERY: " + sql);
        DataSet dataSet = handleQuery(sql);

        try {
            log.debug(String.format("RESULT SET with %s rows", dataSet.getRows().size()));
        } catch (SQLException e) {
            throw new JdbcServerException("Failed to access dataset", e);
        }

        log.info("QUERY EXECUTION SUCCESSFUL");
        return dataSet;
    }

    @Override
    public void execute(String sql) throws JdbcServerException {
        log.info("EXECUTE STATEMENT: " + sql);
        handleUpdate(sql);
        log.info("STATEMENT EXECUTION SUCCESSFUL");
    }

    @Override
    public int executeUpdate(String sql) throws JdbcServerException {
        log.info("EXECUTE UPDATE: " + sql);

        int rows = handleUpdate(sql);

        log.debug(String.format("ROWS UPDATED %s", rows));
        log.info("UPDATE EXECUTION SUCCESSFUL");
        return rows;
    }

    @Override
    public void closeStatement() throws JdbcServerException {
        log.info("CLOSE STATEMENT");
    }
}
