package com.consol.citrus.db.driver.data;

import com.consol.citrus.db.driver.utils.LobUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Objects;

import static java.lang.Math.toIntExact;

/**
 * CLOB implementation of the Citrus JDBC Driver.
 *
 * <b>Caution:</b>
 * This CLOB implementation is limited to String size objects to reduce the memory footprint.
 */
public class CitrusClob implements Clob {

    private final StringBuilder stringBuilder = new StringBuilder();
    private final LobUtils lobUtils = new LobUtils();

    @Override
    public long length() {
        return stringBuilder.length();
    }

    @Override
    public String getSubString(final long pos, final int length) {
        final long longOffset = lobUtils.applyOffset(pos);
        if(lobUtils.fitsInInt(longOffset)){
            final int offset = toIntExact(longOffset);
            return stringBuilder.substring(offset, offset + length);
        }

        return null;
    }

    @Override
    public Reader getCharacterStream() {
        return new StringReader(stringBuilder.toString());
    }

    @Override
    public InputStream getAsciiStream() {
        return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }

    @Override
    public long position(final String searchstr, final long start) {
        if(lobUtils.fitsInInt(start)){
            return stringBuilder.indexOf(searchstr, (int)start);
        }
        return -1;
    }

    @Override
    public long position(final Clob searchstr, final long start) throws SQLException {
        final long clobLength = searchstr.length();
        if(lobUtils.fitsInInt(clobLength)){
            final String subString = searchstr.getSubString(1, (int)clobLength);
            return position(subString, start);
        }
        return -1;
    }

    @Override
    public int setString(final long pos, final String str) {
        return setString(pos, str, 0, str.length());
    }

    @Override
    public int setString(final long pos, final String str, final int offset, final int len) {
        final long positionWithOffset = lobUtils.applyOffset(pos);
        if(lobUtils.fitsInInt(positionWithOffset)){
            return setContent(stringBuilder, (int) positionWithOffset, str, offset, len);
        }

        return 0;
    }

    @Override
    public OutputStream setAsciiStream(final long pos) {
        return new OutputStream() {

            private long position = pos;

            @Override
            public void write(final int b) {
                final byte character = (byte) b;
                setString(position++, new String(new byte[]{character}));
            }
        };
    }

    @Override
    public Writer setCharacterStream(final long pos) {
        return new Writer() {

            private StringBuilder buffer = new StringBuilder();

            @Override
            public void write(final char[] cbuf, final int off, final int len) {
                setContent(buffer, off, new String(cbuf), 0, len);
            }

            @Override
            public void flush() {
                buffer = new StringBuilder();
            }

            @Override
            public void close() {
                setString(pos, buffer.toString());
            }
        };
    }

    @Override
    public void truncate(final long len) {
        if(lobUtils.fitsInInt(len)){
            stringBuilder.delete((int)len, stringBuilder.length());
        }
    }

    @Override
    public void free() {
        stringBuilder.delete(0, stringBuilder.length());
    }

    @Override
    public Reader getCharacterStream(final long pos, final long length) throws SQLException {
        if(pos < 1){
            throw new SQLException("position is < 1 but CLOB starts at 1.");
        }

        if(pos + length > stringBuilder.length()){
            throw new SQLException("CLOB references exceeds actual CLOB size");
        }

        final long posWithOffset = lobUtils.applyOffset(pos);
        if(lobUtils.fitsInInt(posWithOffset + length)){
            final int intPos = (int) posWithOffset;
            final String substring = stringBuilder.substring(intPos, intPos + (int)length);
            return new StringReader(substring);
        }

        return null;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CitrusClob)) return false;
        final CitrusClob that = (CitrusClob) o;
        return  Objects.equals(stringBuilder.toString(), that.stringBuilder.toString());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(stringBuilder);
    }

    /**
     * Alters the {@link StringBuilder} of this @{@link CitrusClob} to contain the given string.
     * If the size of the altered string exceeds the current StringBuilder size, it is automatically extended to the
     * required capacity.
     * @param stringBuilder The string builder to alter
     * @param position The start position for altering the content. Starts at 0.
     * @param stringToSet The String to set the content from.
     * @param offset  the index of the first character of {@code stringToSet} to be inserted. Starting at 0.
     * @param length The length of the string to set.
     * @return The length of the string that has been set
     */
    private int setContent(final StringBuilder stringBuilder,
                           final int position,
                           final String stringToSet,
                           final int offset,
                           final int length) {
        final boolean expandsCurrentContent = position + length > stringBuilder.length();

        if(expandsCurrentContent){
            stringBuilder.delete(position, stringBuilder.length());
            stringBuilder.insert(position, stringToSet.toCharArray(), offset, length);
        }else{
            stringBuilder.replace(position, position+length, stringToSet);
        }
        return length;
    }
}
