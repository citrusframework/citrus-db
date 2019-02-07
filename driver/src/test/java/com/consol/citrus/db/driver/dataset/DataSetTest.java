package com.consol.citrus.db.driver.dataset;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
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

    @Test
    public void testToString(){
        ToStringVerifier.forClass(DataSet.class);
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(DataSet.class);
    }
}