package com.consol.citrus.db.driver.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Objects;

public class CitrusClob implements Clob {

    private final StringBuffer stringBuffer = new StringBuffer();

    @Override
    public long length() throws SQLException {
        return 0;
    }

    @Override
    public String getSubString(final long pos, final int length) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        return null;
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
    public int setString(final long pos, final String str) throws SQLException {
        return 0;
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
        return "CitrusClob{}";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CitrusClob)) return false;
        final CitrusClob that = (CitrusClob) o;
        return Objects.equals(stringBuffer.toString(), that.stringBuffer.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringBuffer);
    }
}
