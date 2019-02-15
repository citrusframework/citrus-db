package com.consol.citrus.db.driver.dataset;

import com.consol.citrus.db.driver.data.Row;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.testng.Assert.assertNull;

public class DataSetTest {

    private DataSet dataSet = new DataSet();

    @Test
    public void emptyDataSetReturnsNullOnGetNextRow() throws SQLException {

        //WHEN
        final Row nextRow = dataSet.getNextRow();

        //THEN
        assertNull(nextRow);
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