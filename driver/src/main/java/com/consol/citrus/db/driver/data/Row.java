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

import org.apache.commons.beanutils.ConvertUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;

public class Row {

    /** Row values with column name as key */
    private Map<String, Object> values = new LinkedHashMap<>();

    private Object lastValue;

    /**
     * Gets set of column names available in this row.
     * @return A list of column values
     */
    public List<String> getColumns() {
        return Arrays.asList(values.keySet().toArray(new String[0]));
    }

    /**
     * Gets the row value identified by its column name.
     * @param columnName The name to get the value for
     * @return The value of that column
     */
    public Object getValue(final String columnName) {
        lastValue = values.get(columnName);
        return lastValue;
    }

    /**
     * Gets the row value identified by its column name converted into the given type
     * @param parameterName The name to get the value for
     * @param clazz The class to convert the value to
     * @param <T> Generic parameter to ensure correct conversion
     * @return The converted object
     */
    public <T> Object getValue(final String parameterName, final Class<T> clazz) {
        final Object value = getValue(parameterName);
        return convertData(value, clazz);
    }

    /**
     * Gets the row value identified by its column index.
     * @param columnIndex The column index to get the value from
     * @return The object of that index
     */
    public Object getValue(final int columnIndex) {
        lastValue = values.values().toArray()[columnIndex];
        return lastValue;
    }

    /**
     * Gets the row value identified by its column index converted into the given type
     * @param columnIndex The index to get the value from
     * @param clazz The class to convert the value to
     * @param <T> Generic parameter to ensure correct conversion
     * @return The converted object
     */
    public  <T> Object getValue(final int columnIndex, final Class<T> clazz){
        final Object value = getValue(columnIndex);
        return convertData(value, clazz);
    }

    /**
     * Gets the values.
     *
     * @return The values of the row identified by column manes
     */
    public Map<String, Object> getValues() {
        return values;
    }

    /**
     * Sets the values.
     *
     * @param values The values to set in the Row
     */
    public void setValues(final SortedMap<String, Object> values) {
        this.values = values;
    }

    private <T> Object convertData(final Object value, final Class<T> clazz) {
        return Objects.isNull(value) ? null : ConvertUtils.convert(value, clazz);
    }

    /**
     * Gets the lastValue.
     * @return The last value as object
     */
    public Object getLastValue() {
        return lastValue;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Row)) return false;
        final Row row = (Row) o;
        return Objects.equals(values, row.values) &&
                Objects.equals(lastValue, row.lastValue);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(values, lastValue);
    }

    @Override
    public String toString() {
        return "Row{" +
                "values=" + values +
                ", lastValue=" + lastValue +
                '}';
    }
}
