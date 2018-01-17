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

package com.consol.citrus.db.driver.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
public class Table {

    private String schema;
    private final String name;

    private List<Row> rows = new ArrayList<>();

    /**
     * Default constructor using name.
     * @param name
     */
    public Table(String name) {
        this.name = name;
    }

    /**
     * Default constructor using schema and name.
     * @param schema
     * @param name
     */
    public Table(String schema, String name) {
        this(name);
        this.schema = schema;
    }

    /**
     * Gets the name.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the schema.
     *
     * @return
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the schema.
     *
     * @param schema
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * Gets the columns.
     *
     * @return
     */
    public List<String> getColumns() {
        return rows.stream().flatMap(row -> row.getColumns().stream()).distinct().collect(Collectors.toList());
    }

    /**
     * Gets the rows.
     *
     * @return
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * Sets the rows.
     *
     * @param rows
     */
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
