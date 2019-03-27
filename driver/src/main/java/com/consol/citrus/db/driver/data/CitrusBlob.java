package com.consol.citrus.db.driver.data;

import org.apache.commons.lang3.ArrayUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.Arrays;

public class CitrusBlob implements Blob {

    private byte[] content = new byte[0];

    @Override
    public long length() {
        return 0;
    }

    @Override
    public byte[] getBytes(final long pos, final int length) {
        return new byte[0];
    }

    @Override
    public InputStream getBinaryStream() {
        return null;
    }

    @Override
    public long position(final byte[] pattern, final long start) {
        return 0;
    }

    @Override
    public long position(final Blob pattern, final long start) {
        return 0;
    }

    @Override
    public int setBytes(final long pos, final byte[] bytes) {
        //todo: improve implementation, see CitrusClob
        if(content.length <= pos + bytes.length){
            final byte[] untouchedContent = Arrays.copyOfRange(content,0, (int)pos-1);
            content = ArrayUtils.addAll(untouchedContent, bytes);
        }else{
            content = ArrayUtils.insert((int)pos-1, content, bytes);
        }

        return bytes.length;
    }

    @Override
    public int setBytes(final long pos, final byte[] bytes, final int offset, final int len) {
        return 0;
    }

    @Override
    public OutputStream setBinaryStream(final long pos) {
        return null;
    }

    @Override
    public void truncate(final long len) {

    }

    @Override
    public void free() {

    }

    @Override
    public InputStream getBinaryStream(final long pos, final long length) {
        return null;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CitrusBlob)) return false;
        final CitrusBlob that = (CitrusBlob) o;
        return Arrays.equals(content, that.content);
    }

    @Override
    public final int hashCode() {
        return Arrays.hashCode(content);
    }

    @Override
    public String toString() {
        return Arrays.toString(content);
    }
}
