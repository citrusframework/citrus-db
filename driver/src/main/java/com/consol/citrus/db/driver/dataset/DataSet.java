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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataSet {

    /** Rows in this data set */
    private final List<Row> rows = new ArrayList<>();

    /** Cursor position on selected row */
    private AtomicInteger cursor = new AtomicInteger(0);

    public DataSet() {
    }

    @JsonCreator
    public DataSet(@JsonProperty("rows") final List<Row> rows){
        this.rows.addAll(rows);
    }

    /**
     * Gets next row in this data set based on cursor position.
     * If there is no further row, the index cursor position stays untouched
     * @return The next row of the dataset or null if no further row is available
     */
    @JsonIgnore
    public Row getNextRow(){
        final int index = cursor.getAndIncrement();
        if(rows.size() <= index){
            cursor.set(cursor.get()-1);
            return null;
        }

        return rows.get(index);
    }

    /**
     * Gets list of columns in this dataset.
     * @return The list of columns of the dataset
     */
    @JsonIgnore
    public List<String> getColumns() {
        return rows.stream().flatMap(row -> row.getColumns().stream()).distinct().collect(Collectors.toList());
    }

    /**
     * Gets all rows in this dataset.
     * @return The rows of the Dataset
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * Gets current row index.
     * @return The current curser of the DataSet
     */
    @JsonIgnore
    public int getCursor() {
        return cursor.get();
    }

    @Override
    @JsonIgnore
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DataSet)) return false;
        final DataSet dataSet = (DataSet) o;
        return Objects.equals(rows, dataSet.rows);
    }

    @Override
    @JsonIgnore
    public final int hashCode() {
        return Objects.hash(rows);
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "DataSet{" +
                "rows=" + rows +
                ", cursor=" + cursor +
                '}';
    }
}
