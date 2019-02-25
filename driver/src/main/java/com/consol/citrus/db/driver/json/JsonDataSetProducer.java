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

package com.consol.citrus.db.driver.json;

import com.consol.citrus.db.driver.JdbcDriverException;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.DataSetProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * @author Christoph Deppisch
 */
public class JsonDataSetProducer implements DataSetProducer {

    /** Json data used as table source */
    private final InputStream input;

    public JsonDataSetProducer(File file) {
        this(file.toPath());
    }

    public JsonDataSetProducer(Path path) {
        try {
            this.input = new FileInputStream(path.toFile());
        } catch (FileNotFoundException e) {
            throw new JdbcDriverException("Failed to access json input file content", e);
        }
    }

    public JsonDataSetProducer(String jsonInput) {
        this.input = new ByteArrayInputStream(jsonInput.getBytes());
    }

    public JsonDataSetProducer(InputStream inputStream) {
        this.input = inputStream;
    }

    @Override
    public DataSet produce() throws SQLException {
        try {
            return new ObjectMapper().readValue(input, DataSet.class);
        } catch (IOException e) {
            throw new JdbcDriverException("Unable to read table data set from Json input", e);
        }
    }
}
