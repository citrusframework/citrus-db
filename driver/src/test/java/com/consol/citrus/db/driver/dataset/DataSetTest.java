package com.consol.citrus.db.driver.dataset;

import org.testng.annotations.Test;

import java.sql.SQLException;

public class DataSetTest {

    private DataSet dataSet = new DataSet();

    @Test(expectedExceptions = SQLException.class)
    public void emptyDataSetThrowsExceptionOnGetNextRow() throws SQLException {

        //WHEN
        dataSet.getNextRow();

        //THEN
        //exception
    }

}