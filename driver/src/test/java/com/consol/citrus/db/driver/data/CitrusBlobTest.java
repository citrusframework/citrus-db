package com.consol.citrus.db.driver.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

public class CitrusBlobTest {

    @Test
    public void testEqualsContract(){
        EqualsVerifier
                .forClass(CitrusBlob.class)
                .suppress(Warning.NONFINAL_FIELDS) //Blob content has to be mutable
                .verify();
    }

    @Test
    public void testToString(){

        //GIVEN
        final byte[] expectedClobContent = "Beam Me Up, Scotty".getBytes();
        final CitrusBlob citrusBlob = new CitrusBlob();
        citrusBlob.setBytes(1, expectedClobContent);

        //WHEN
        final String clobContent = citrusBlob.toString();

        //THEN
        assertEquals(clobContent, Arrays.toString(expectedClobContent));
    }

}