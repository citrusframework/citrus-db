package com.consol.citrus.db.driver.data;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
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
    public void testGetAsciiStream() throws Exception{

        //GIVEN
        citrusClob.setString(1, sampleText);

        //WHEN
        final InputStream asciiStream = citrusClob.getAsciiStream();

        //THEN
        final String clob = IOUtils.toString(asciiStream, Charset.forName("UTF8"));
        assertEquals(clob, sampleText);
    }

    @Test
    public void testPositionWithStringNeedle() throws Exception{

        //GIVEN
        citrusClob.setString(1, sampleText);

        //WHEN
        final long position = citrusClob.position("and", 1);

        //THEN
        assertEquals(position, 10);
    }

    @Test
    public void testPositionWithStringNeedleWithoutMatch() throws Exception{

        //GIVEN
        citrusClob.setString(1, sampleText);

        //WHEN
        final long position = citrusClob.position("and", 13);

        //THEN
        assertEquals(position, -1);
    }

    @Test
    public void testPositionWithStringNeedleAndLongPositionReturnsMinusOne() throws Exception{

        //GIVEN
        citrusClob.setString(1, sampleText);

        //WHEN
        final long position = citrusClob.position("and", Long.MAX_VALUE);

        //THEN
        assertEquals(position, -1);
    }

    @Test
    public void testPositionWithClobNeedle() throws Exception{

        //GIVEN
        final CitrusClob needle = createClobNeedle();
        citrusClob.setString(1, sampleText);


        //WHEN
        final long position = citrusClob.position(needle, 1);

        //THEN
        assertEquals(position, 10);
    }

    @Test
    public void testPositionWithClobNeedleWithoutMatch() throws Exception{

        //GIVEN
        final CitrusClob needle = createClobNeedle();
        citrusClob.setString(1, sampleText);

        //WHEN
        final long position = citrusClob.position(needle, 13);

        //THEN
        assertEquals(position, -1);
    }

    @Test
    public void testPositionWithClobNeedleAndLongPositionReturnsMinusOne() throws Exception{

        //GIVEN
        final CitrusClob needle = createClobNeedle();
        citrusClob.setString(1, sampleText);

        //WHEN
        final long position = citrusClob.position(needle, Long.MAX_VALUE);

        //THEN
        assertEquals(position, -1);
    }

    @Test
    public void testSetStringWithOffsetAndLengthExtendingContent() throws Exception{

        //GIVEN
        citrusClob.setString(1, sampleText);

        final String textToAdd = "just calm down and kill zombies and stuff";

        final String expectedClobContent = "Keep calm and kill zombies";

        //WHEN
        final long writtenCharacters = citrusClob.setString(15, textToAdd, 19, 12);

        //THEN
        final String clobContent = IOUtils.toString(citrusClob.getCharacterStream());
        assertEquals(clobContent, expectedClobContent);
        assertEquals(writtenCharacters, 12);
    }

    @Test
    public void testSetStringWithOffsetAndLengthReplacingContent() throws Exception{

        //GIVEN
        citrusClob.setString(1, sampleText);

        final String textToAdd = "but";

        final String expectedClobContent = "Keep calm but allons-y";

        //WHEN
        final long writtenCharacters = citrusClob.setString(11, textToAdd, 0, 3);

        //THEN
        final String clobContent = IOUtils.toString(citrusClob.getCharacterStream());
        assertEquals(clobContent, expectedClobContent);
        assertEquals(writtenCharacters, 3);
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

    private CitrusClob createClobNeedle() {
        final CitrusClob needle = new CitrusClob();
        needle.setString(1, "and");
        return needle;
    }
}