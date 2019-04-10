package com.consol.citrus.db.driver.utils;

import com.consol.citrus.db.driver.data.CitrusClob;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

public class LobUtils {

    public boolean fitsInInt(final long value) {
        return (int)value == value;
    }

    /**
     * Creates a {@link CitrusClob} from the given {@link Reader}
     * @param reader The reader to create the clob from
     * @param length The length of the substring to take from the reader or -1 to transfer the whole content
     * @return The created Citrus Clob
     * @throws SQLException In case the clob could not be created from the reader
     */
    public CitrusClob createClobFromReader(final Reader reader, final int length) throws SQLException {
        try{
            final String desiredClobContent = IOUtils.toString(reader);
            return createClob(length, desiredClobContent);
        } catch (final IOException e) {
            throw new SQLException("Could not create Clob from reader", e);
        }
    }

    public long applyOffset(final long pos) {
        return pos - 1;
    }

    private CitrusClob createClob(final int length, final String desiredClobContent) {
        final CitrusClob citrusClob = new CitrusClob();
        if(length >= 0){
            citrusClob.setString(1, desiredClobContent.substring(0, length));
        }else{
            citrusClob.setString(1, desiredClobContent);
        }
        return citrusClob;
    }
}
