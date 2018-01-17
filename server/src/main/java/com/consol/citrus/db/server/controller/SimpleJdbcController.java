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

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
public class SimpleJdbcController implements JdbcController {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(SimpleJdbcController.class);

    @Override
    public void getConnection(Map<String, String> properties) throws JdbcServerException {
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
    public void createPreparedStatement(String stmt) throws JdbcServerException {
        log.info("CREATE PREPARED STATEMENT: " + stmt);
    }

    @Override
    public DataSet executeQuery(String stmt) throws JdbcServerException {
        log.info("EXECUTE QUERY: " + stmt);
        log.info("Return empty result set");
        return new DataSet();
    }

    @Override
    public void execute(String stmt) throws JdbcServerException {
        log.info("EXECUTE STATEMENT: " + stmt);
    }

    @Override
    public int executeUpdate(String stmt) throws JdbcServerException {
        log.info("EXECUTE UPDATE: " + stmt);
        log.info("Return 0 affected rows");
        return 0;
    }

    @Override
    public void closeStatement() throws JdbcServerException {
        log.info("CLOSE STATEMENT");
    }
}
