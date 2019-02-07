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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Christoph Deppisch
 */
public class DataSet {

    /** Rows in this data set */
    private final List<Row> rows = new ArrayList<>();

    /** Indicates that this data set is closed */
    private boolean closed = false;

    /** Cursor position on selected row */
    private AtomicInteger cursor = new AtomicInteger(0);

    /**
     * Gets next row in this result set based on cursor position.
     * @return The next row of the dataset
     * @throws SQLException In case the DataSet has been closed or no further rows are available
     */
    public Row getNextRow() throws SQLException {
        checkNotClosed();

        final int index = cursor.getAndIncrement();
        if(rows.size() <= index){
            return null;
        }

        return rows.get(index);
    }

    /**
     * Gets list of columns in this dataset.
     * @return The list of columns of the dataset
     */
    public List<String> getColumns() {
        return rows.stream().flatMap(row -> row.getColumns().stream()).distinct().collect(Collectors.toList());
    }

    /**
     * Gets all rows in this dataset.
     * @return The rows of the Dataset
     * @throws SQLException in case the DataSet has been closed
     */
    public List<Row> getRows() throws SQLException {
        checkNotClosed();
        return rows;
    }

    /**
     * Gets current row index.
     * @return The current curser of the DataSet
     */
    public int getCursor() {
        return cursor.get();
    }

    /**
     * Close result set - no further access to rows and columns allowed.
     */
    public void close() {
        this.closed = true;
    }

    /**
     * If closed throws new SQLException.
     * @throws SQLException In case the DataSet has been closed
     */
    private void checkNotClosed() throws SQLException {
        if (closed) {
            throw new SQLException("Result set already closed");
        }
    }

    /**
     * Returns <code>true</code> if the data set is already closed, otherwise <code>false</code>.
     * @return Whether the DataSet has been closed
     */
    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DataSet)) return false;
        final DataSet dataSet = (DataSet) o;
        return closed == dataSet.closed &&
                Objects.equals(rows, dataSet.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, closed, cursor);
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "rows=" + rows +
                ", closed=" + closed +
                ", cursor=" + cursor +
                '}';
    }
}
