package com.consol.citrus.db.driver.exchange;

import com.consol.citrus.db.driver.dataset.DataSet;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class DatabaseResult {

    private final DataSet dataSet;

    /** The affected rows of the database operation*/
    private final int affectedRows;

    private final boolean isDataSet;

    public DatabaseResult(final DataSet dataSet) {
        this.dataSet = dataSet;
        this.affectedRows = -1;
        isDataSet = true;
    }

    public DatabaseResult(final int affectedRows) {
        this.dataSet = null;
        this.affectedRows = affectedRows;
        isDataSet = false;
    }

    @JsonCreator
    public DatabaseResult(@JsonProperty("dataSet") final DataSet dataSet,
                          @JsonProperty("affectedRows") final int affectedRows,
                          @JsonProperty("isDataSet") final boolean isDataSet) {
        this.dataSet = dataSet;
        this.affectedRows = affectedRows;
        this.isDataSet = isDataSet;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    @JsonGetter("dataSet")
    public DataSet getDataSet() {
        return dataSet;
    }

    @JsonGetter("isDataSet")
    public boolean isDataSet(){
        return isDataSet;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DatabaseResult)) return false;
        final DatabaseResult that = (DatabaseResult) o;
        return affectedRows == that.affectedRows &&
                Objects.equals(dataSet, that.dataSet) &&
                Objects.equals(isDataSet, that.isDataSet);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(dataSet, affectedRows,isDataSet);
    }

    @Override
    public String toString() {
        return "DatabaseResult{" +
                "dataSet=" + dataSet +
                ", affectedRows=" + affectedRows +
                ", isDataSet=" + isDataSet +
                '}';
    }
}
