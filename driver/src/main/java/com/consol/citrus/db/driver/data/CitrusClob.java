package com.consol.citrus.db.driver.data;

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

    @Override
    public long length() {
        return stringBuilder.length();
    }

    @Override
    public String getSubString(final long pos, final int length) {
        final long longOffset = applyOffset(pos);
        if(fitsInInt(longOffset)){
            final int offset = toIntExact(longOffset);
            return stringBuilder.substring(offset, offset + length);
        }

        return null;
    }

    private long applyOffset(long pos) {
        return pos - 1;
    }

    @Override
    public Reader getCharacterStream() {
        return new StringReader(stringBuilder.toString());
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        return null;
    }

    @Override
    public long position(final String searchstr, final long start) throws SQLException {
        return 0;
    }

    @Override
    public long position(final Clob searchstr, final long start) throws SQLException {
        return 0;
    }

    @Override
    public int setString(final long pos, final String str) {
        final long offset = applyOffset(pos);
        if(fitsInInt(offset)){
            stringBuilder.insert((int) offset, str);
            return str.length();
        }else {
            return 0;
        }

    }

    @Override
    public int setString(final long pos, final String str, final int offset, final int len) throws SQLException {
        return 0;
    }

    @Override
    public OutputStream setAsciiStream(final long pos) throws SQLException {
        return null;
    }

    @Override
    public Writer setCharacterStream(final long pos) throws SQLException {
        return null;
    }

    @Override
    public void truncate(final long len) throws SQLException {

    }

    @Override
    public void free() throws SQLException {

    }

    @Override
    public Reader getCharacterStream(final long pos, final long length) throws SQLException {
        return null;
    }

    @Override
    public String toString() {
        return "CitrusClob{" +
                "stringBuilder=" + stringBuilder +
                '}';
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

    private boolean fitsInInt(final long value) {
        return (int)value == value;
    }
}
