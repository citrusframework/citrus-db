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
import com.consol.citrus.db.driver.data.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Christoph Deppisch
 */
public class JsonTableProducer implements TableProducer {

    /** Json data used as table source */
    private final InputStream input;

    public JsonTableProducer(File file) {
        this(file.toPath());
    }

    public JsonTableProducer(Path path) {
        try {
            this.input = new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new JdbcDriverException(e);
        }
    }

    public JsonTableProducer(String jsonInput) {
        this.input = new ByteArrayInputStream(jsonInput.getBytes());
    }

    public JsonTableProducer(InputStream inputStream) {
        this.input = inputStream;
    }

    @Override
    public List<Table> produce() {
        List<Table> tables = new ArrayList<>();

        try {
            Map<String, List<Map<String, String>>> rawDataSet = new ObjectMapper().readValue(input, Map.class);

            for (Map.Entry<String, List<Map<String, String>>> tableData : rawDataSet.entrySet()) {
                Table table = new Table(tableData.getKey());

                tableData.getValue().forEach(rowData -> {
                    Row row = new Row();
                    row.getValues().putAll(rowData);
                    table.getRows().add(row);
                });

                tables.add(table);
            }
        } catch (IOException e) {
            throw new JdbcDriverException("Unable to read table data set from Json input", e);
        }

        return tables;
    }
}
