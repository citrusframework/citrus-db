package com.consol.citrus.db.driver.exchange;

import com.consol.citrus.db.driver.dataset.DataSet;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.testng.annotations.Test;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class DatabaseResultTest {


    @Test
    public void testAffectedRowsConstructor() {

        //GIVEN
        final int expectedAffectedRows = 42;

        //WHEN
        final DatabaseResult databaseResult = new DatabaseResult(expectedAffectedRows);

        //THEN
        assertFalse(databaseResult.isDataSet());
        assertEquals(databaseResult.getAffectedRows(), expectedAffectedRows);
        assertNull(databaseResult.getDataSet());
    }

    @Test
    public void testDataSetConstructor() {

        //GIVEN
        final DataSet dataSet = mock(DataSet.class);

        //WHEN
        final DatabaseResult databaseResult = new DatabaseResult(dataSet);

        //THEN
        assertTrue(databaseResult.isDataSet());
        assertEquals(databaseResult.getAffectedRows(), -1);
        assertEquals(databaseResult.getDataSet(), dataSet);
    }

    @Test
    public void testJsonConstructor() {

        //GIVEN
        final DataSet dataSet = mock(DataSet.class);
        final int expectedAffectedRows = 42;
        final boolean isDataSet = true;

        //WHEN
        final DatabaseResult databaseResult = new DatabaseResult(dataSet, expectedAffectedRows, isDataSet);

        //THEN
        assertEquals(databaseResult.isDataSet(), isDataSet);
        assertEquals(databaseResult.getAffectedRows(), expectedAffectedRows);
        assertEquals(databaseResult.getDataSet(), dataSet);
    }


    @Test
    public void testToString(){
        ToStringVerifier.forClass(DatabaseResult.class).verify();
    }

    @Test
    public void testEqualsContract(){
        EqualsVerifier
                .forClass(DatabaseResult.class)
                .verify();
    }
}