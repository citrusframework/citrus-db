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

package com.consol.citrus.db.driver.dataset;

import com.consol.citrus.db.driver.data.Row;
import com.consol.citrus.db.driver.data.Table;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Christoph Deppisch
 */
public class TableDataSetProducer implements DataSetProducer {

    private final Table table;
    private int limit;

    private Function<Row, Row> mapper = (row) -> row;
    private Map<String, String> filter = new LinkedHashMap<>();

    public TableDataSetProducer(Table table) {
        this(table, table.getRows().size());
    }

    public TableDataSetProducer(Table table, String ... columnNames) {
        this(table, table.getRows().size(), columnNames);
    }

    public TableDataSetProducer(Table table, int limit, String ... columnNames) {
        this.table = table;
        this.limit = limit;

        if (columnNames.length > 0) {
            mapper = (row -> {
                Row mapped = new Row();
                for (String name : columnNames) {
                    mapped.getValues().put(name, row.getValues()
                            .entrySet()
                            .stream()
                            .filter(entry -> entry.getKey().equals(name))
                            .map(Map.Entry::getValue)
                            .findFirst()
                            .orElse(""));
                }
                return mapped;
            });
        }
    }

    /**
     * Add filter predicate on column name and value.
     * @param key
     * @param value
     * @return
     */
    public TableDataSetProducer filter(String key, String value) {
        this.filter.put(key, value);
        return this;
    }

    @Override
    public DataSet produce() throws SQLException {
        DataSetBuilder builder = new DataSetBuilder();
        table.getRows()
                .stream()
                .filter(row -> filter.entrySet()
                                    .stream()
                                    .allMatch(entry -> row.getValues().containsKey(entry.getKey()) && row.getValue(entry.getKey()).equals(entry.getValue())))
                .limit(limit)
                .map(mapper)
                .forEach(builder::add);
        return builder.build();
    }

    /**
     * Gets the limit.
     *
     * @return
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }
}
