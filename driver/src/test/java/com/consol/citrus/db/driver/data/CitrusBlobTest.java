package com.consol.citrus.db.driver.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

public class CitrusBlobTest {

    private final CitrusBlob citrusBlob = new CitrusBlob();

    private final byte[] sampleBytes = "Beam Me Up, Scotty".getBytes();

    @Test
    public void testEqualsContract(){
        EqualsVerifier
                .forClass(CitrusBlob.class)
                .suppress(Warning.NONFINAL_FIELDS) //Blob content has to be mutable
                .withIgnoredFields("lobUtils") //Stateless
                .verify();
    }

    @Test
    public void testToString(){

        //GIVEN
        citrusBlob.setBytes(1, sampleBytes);

        //WHEN
        final String clobContent = citrusBlob.toString();

        //THEN
        assertEquals(clobContent, Arrays.toString(sampleBytes));
    }

    @Test
    public void testGetBytes() {

        //GIVEN
        citrusBlob.setBytes(1, sampleBytes);
        final byte[] expectedBlobContent = "Me Up".getBytes();

        //WHEN
        final byte[] blobContent = citrusBlob.getBytes(6, 5);

        //THEN
        final String blobString = new String(blobContent);
        assertEquals(blobString, new String(expectedBlobContent));
    }

    @Test
    public void testLength() {

        //GIVEN
        citrusBlob.setBytes(1, sampleBytes);

        //WHEN
        final long length = citrusBlob.length();
        //THEN
        assertEquals(length, sampleBytes.length);
    }

    @Test
    public void testSetStringWithOffsetAndLengthReplacingContent() {

        //GIVEN
        citrusBlob.setBytes(1, sampleBytes);

        final byte[] bytesToAdd = "pause".getBytes();

        final byte[] expectedBlobContent = "Beam us Up, Scotty".getBytes();

        //WHEN
        final long writtenBytes = citrusBlob.setBytes(6, bytesToAdd, 2, 2);

        //THEN
        final String blobString = new String(citrusBlob.getBytes(1, (int)citrusBlob.length()));
        assertEquals(blobString, new String(expectedBlobContent));
        assertEquals(writtenBytes, 2);
    }

}