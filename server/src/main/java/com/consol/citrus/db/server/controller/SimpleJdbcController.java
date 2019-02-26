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
import com.consol.citrus.db.driver.dataset.DataSetProducer;
import com.consol.citrus.db.driver.exchange.DatabaseResult;
import com.consol.citrus.db.server.JdbcServerException;

import java.sql.SQLException;

/**
 * @author Christoph Deppisch
 */
public class SimpleJdbcController extends AbstractJdbcController {

    private final DataSetProducer dataSetProducer;

    /**
     * Default constructor using default dataSet producer.
     */
    public SimpleJdbcController() {
        this(DataSet::new);
    }

    /**
     * Constructor initializes dataSet producer.
     * @param dataSetProducer The producer to create dataSets
     */
    public SimpleJdbcController(final DataSetProducer dataSetProducer) {
        this.dataSetProducer = dataSetProducer;
    }

    @Override
    protected DatabaseResult handleQuery(final String sql){
        try {
            return new DatabaseResult(dataSetProducer.produce());
        } catch (final SQLException e) {
            throw new JdbcServerException("Failed to produce dataSet", e);
        }
    }

    @Override
    protected DatabaseResult handleExecute(final String sql){
        try {
            return new DatabaseResult(dataSetProducer.produce());
        } catch (final SQLException e) {
            throw new JdbcServerException("Failed to produce dataSet", e);
        }
    }

    @Override
    protected int handleUpdate(final String sql){
        return 0;
    }
}
