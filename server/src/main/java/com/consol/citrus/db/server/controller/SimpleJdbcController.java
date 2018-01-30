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
import com.consol.citrus.db.server.JdbcServerException;

import java.sql.SQLException;

/**
 * @author Christoph Deppisch
 */
public class SimpleJdbcController extends AbstractJdbcController {

    private final DataSetProducer dataSetProducer;
    private boolean transactionState;

    /**
     * Default constructor using default dataset producer.
     */
    public SimpleJdbcController() {
        this(DataSet::new);
    }

    /**
     * Constructor initializes dataset producer.
     * @param dataSetProducer
     */
    public SimpleJdbcController(DataSetProducer dataSetProducer) {
        this.dataSetProducer = dataSetProducer;
    }

    @Override
    protected DataSet handleQuery(String sql) throws JdbcServerException {
        try {
            return dataSetProducer.produce();
        } catch (SQLException e) {
            throw new JdbcServerException("Failed to produce datase", e);
        }
    }

    @Override
    protected int handleUpdate(String sql) throws JdbcServerException {
        return 0;
    }

    @Override
    public void setTransactionState(final boolean transactionState) {
        this.transactionState = transactionState;
    }

    @Override
    public boolean getTransactionState() {
        return this.transactionState;
    }

    @Override
    public void commitStatements() {

    }

    @Override
    public void rollbackStatements() {

    }
}
