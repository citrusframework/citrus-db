package com.consol.citrus.db.driver.data;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Clob;
import java.sql.SQLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class CitrusClobTest {

    private Clob citrusClob;
    private String sampleText = "Keep calm and allons-y";
    private int lengthOfSampleText = sampleText.length();

    @BeforeMethod
    public void setUp(){
        citrusClob = new CitrusClob();
    }

    @Test
    public void testSetString() throws Exception {

        //WHEN
        final int numberOfWrittenCharacters = citrusClob.setString(1, sampleText);

        //THEN
        final String clobContent = IOUtils.toString(citrusClob.getCharacterStream());
        assertEquals(clobContent, sampleText);
        assertEquals(numberOfWrittenCharacters, lengthOfSampleText);
    }

    @Test
    public void testSetStringWithLongPositionIsNoop() throws Exception {

        //WHEN
        final int numberOfWrittenCharacters = citrusClob.setString(Long.MAX_VALUE, sampleText);

        //THEN
        final String clobContent = IOUtils.toString(citrusClob.getCharacterStream());
        assertTrue(clobContent.isEmpty());
        assertEquals(numberOfWrittenCharacters, 0);
    }


    @Test
    public void testLength() throws SQLException {

        //WHEN
        citrusClob.setString(1, sampleText);

        //THEN
        assertEquals(citrusClob.length(), lengthOfSampleText);
    }

    @Test
    public void testSubString() throws SQLException {

        //GIVEN
        citrusClob.setString(1, sampleText);
        final String expectedSubString = "allons-y";

        //WHEN
        final String subString = citrusClob.getSubString(15, 8);

        //THEN
        assertEquals(subString, expectedSubString);
    }

    @Test
    public void testSubStringWithLongPositionReturnsNull() throws SQLException {

        //GIVEN
        citrusClob.setString(1, sampleText);

        //WHEN
        final String subString = citrusClob.getSubString(Long.MAX_VALUE, 8);

        //THEN
        assertNull(subString);
    }

    @Test
    public void testEqualsContract(){
        final StringBuilder one = new StringBuilder();
        one.append("foo");
        final StringBuilder two = new StringBuilder();
        one.append("bar");

        EqualsVerifier
                .forClass(CitrusClob.class)
                .withNonnullFields("stringBuilder")
                .withPrefabValues(StringBuilder.class, one, two)
                .verify();
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(CitrusClob.class).verify();
    }
}