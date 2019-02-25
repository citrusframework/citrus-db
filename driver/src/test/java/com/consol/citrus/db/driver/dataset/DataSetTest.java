package com.consol.citrus.db.driver.dataset;

import com.consol.citrus.db.driver.data.Row;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DataSetTest {

    @Test
    public void testEmptyDataSetReturnsNullOnGetNextRow() {

        //GIVEN
        final DataSet dataSet = new DataSet();

        //WHEN
        final Row nextRow = dataSet.getNextRow();

        //THEN
        assertNull(nextRow);
    }

    @Test
    public void testGetAffectedRows() {

        //GIVEN
        final int expectedAffectedRows = 42;

        final DataSet dataSet = new DataSet();
        dataSet.setAffectedRows(expectedAffectedRows);

        //WHEN
        final int affectedRows = dataSet.getAffectedRows();

        //THEN
        assertEquals(affectedRows, expectedAffectedRows);
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(DataSet.class).verify();
    }

    @Test
    public void testEqualsContract(){
        EqualsVerifier
                .forClass(DataSet.class)
                .withIgnoredFields("cursor")
                .verify();
    }
}