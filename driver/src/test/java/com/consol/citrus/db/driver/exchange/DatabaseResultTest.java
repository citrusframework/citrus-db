package com.consol.citrus.db.driver.exchange;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DatabaseResultTest {


    @Test
    public void testGetAffectedRows() {

        //GIVEN
        final int expectedAffectedRows = 42;
        final DatabaseResult databaseResult = new DatabaseResult(expectedAffectedRows);

        //WHEN
        final int affectedRows = databaseResult.getAffectedRows();

        //THEN
        assertEquals(affectedRows, expectedAffectedRows);
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