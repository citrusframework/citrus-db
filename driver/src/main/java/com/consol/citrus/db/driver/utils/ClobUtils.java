package com.consol.citrus.db.driver.utils;

import com.consol.citrus.db.driver.data.CitrusClob;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

public class ClobUtils {

    public boolean fitsInInt(final long value) {
        return (int)value == value;
    }

    public CitrusClob createClobFromReader(final Reader reader, final int length) throws SQLException {
        try{
            final CitrusClob citrusClob = new CitrusClob();
            final String desiredClobContent = IOUtils.toString(reader);
            citrusClob.setString(1, desiredClobContent.substring(0, length));
            return citrusClob;
        } catch (final IOException e) {
            throw new SQLException("Could not create Clob from reader", e);
        }
    }
}
