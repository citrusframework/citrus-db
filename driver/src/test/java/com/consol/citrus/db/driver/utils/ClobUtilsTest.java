package com.consol.citrus.db.driver.utils;

import com.consol.citrus.db.driver.data.CitrusClob;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.StringReader;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ClobUtilsTest {

    private ClobUtils clobUtils = new ClobUtils();

    @Test
    public void testFitsInInt(){

        //GIVEN
        final long number = 42L;

        //WHEN
        final boolean fitsInInt = clobUtils.fitsInInt(number);

        //THEN
        assertTrue(fitsInInt);
    }

    @Test
    public void testFitsNotInInt(){

        //GIVEN
        final long number = Long.MAX_VALUE;

        //WHEN
        final boolean fitsInInt = clobUtils.fitsInInt(number);

        //THEN
        assertFalse(fitsInInt);
    }

    @Test
    public void setLimitedClobFromReader() throws Exception {

        //GIVEN
        final StringReader stringReader = new StringReader("Stay positive, always!");
        final String expectedClobContent = "Stay positive";

        //WHEN
        final CitrusClob clobFromReader = clobUtils.createClobFromReader(stringReader, 13);

        //THEN
        final String clobContent = IOUtils.toString(clobFromReader.getCharacterStream());
        assertEquals(clobContent, expectedClobContent);
    }

    @Test
    public void setUnlimitedClobFromReader() throws Exception {

        //GIVEN
        final String expectedClobContent = "Stay positive, always!";
        final StringReader stringReader = new StringReader(expectedClobContent);

        //WHEN
        final CitrusClob clobFromReader = clobUtils.createClobFromReader(stringReader, -1);

        //THEN
        final String clobContent = IOUtils.toString(clobFromReader.getCharacterStream());
        assertEquals(clobContent, expectedClobContent);
    }
}