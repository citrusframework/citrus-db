package com.consol.citrus.db.driver.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

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
        assertEquals(clobContent, Base64.encodeBase64String(sampleBytes));
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

    @Test
    public void testGetBinaryStream() throws IOException {

        //GIVEN
        citrusBlob.setBytes(1, sampleBytes);

        //WHEN
        final InputStream binaryStream = citrusBlob.getBinaryStream();

        //THEN
        final byte[] blobContent = IOUtils.toByteArray(binaryStream);
        assertEquals(blobContent, sampleBytes);
    }

    @Test
    public void testTruncate() throws IOException {

        //GIVEN
        citrusBlob.setBytes(1, sampleBytes);
        final byte[] expectedBlobContent = "Beam Me Up".getBytes();

        //WHEN
        citrusBlob.truncate(10);

        //THEN
        final InputStream binaryStream = citrusBlob.getBinaryStream();
        final byte[] blobContent = IOUtils.toByteArray(binaryStream);
        assertEquals(blobContent, expectedBlobContent);
    }

    @Test
    public void testFree() throws IOException {

        //GIVEN
        citrusBlob.setBytes(1, sampleBytes);

        //WHEN
        citrusBlob.free();

        //THEN
        final InputStream binaryStream = citrusBlob.getBinaryStream();
        final byte[] blobContent = IOUtils.toByteArray(binaryStream);
        assertEquals(blobContent, ArrayUtils.EMPTY_BYTE_ARRAY);
    }
}