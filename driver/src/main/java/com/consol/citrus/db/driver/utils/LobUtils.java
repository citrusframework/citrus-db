package com.consol.citrus.db.driver.utils;

import com.consol.citrus.db.driver.data.CitrusBlob;
import com.consol.citrus.db.driver.data.CitrusClob;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Creates a {@link CitrusBlob} from the given {@link java.io.InputStream}
     * @param inputStream The inputStream to create the clob from
     * @param length The length of the data to take from the stream or -1 to transfer the whole content
     * @return The created Citrus Blob
     * @throws SQLException In case the blob could not be created from the reader
     */
    public CitrusBlob createBlobFromInputStream(final InputStream inputStream, final int length) throws SQLException {
        try{
            return createBlob(length, IOUtils.toByteArray(inputStream));
        } catch (final IOException e) {
            throw new SQLException("Could not create Clob from reader", e);
        }
    }

    public long applyOffset(final long pos) {
        return pos - 1;
    }

    private CitrusBlob createBlob(final int length, final byte[] desiredBlobContent) {
        final CitrusBlob citrusBlob = new CitrusBlob();
        if(length >= 0){
            citrusBlob.setBytes(1, ArrayUtils.subarray(desiredBlobContent, 0, length));
        }else{
            citrusBlob.setBytes(1, desiredBlobContent);
        }
        return citrusBlob;
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
