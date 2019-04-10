package com.consol.citrus.db.driver.data;

import com.consol.citrus.db.driver.utils.LobUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.Arrays;

public class CitrusBlob implements Blob {

    private byte[] content = new byte[0];
    private final LobUtils lobUtils = new LobUtils();

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
        final long positionWithOffset = lobUtils.applyOffset(pos);
        if(lobUtils.fitsInInt(positionWithOffset)){
            return setContent(content, (int)positionWithOffset, bytes, 0, bytes.length);
        }

        return 0;
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

    /**
     * Alters the content of this @{@link CitrusBlob} to contain the given bytes.
     * If the size of the altered bytes exceeds the current capacity, the capacity is automatically extended to the
     * required size.
     * @param byteContent The byte content to alter
     * @param position The start position for altering the content. Starts at 0.
     * @param bytesToSet The bytes to set the content from.
     * @param offset  the index of the first character of {@code bytesToSet} to be inserted. Starting at 0.
     * @param length The number of bytes to set.
     * @return The number of the bytes that have been set
     */
    private int setContent(final byte[] byteContent,
                           final int position,
                           final byte[] bytesToSet,
                           final int offset,
                           final int length) {
        final boolean expandsCurrentContent = position + length > byteContent.length;

        if(expandsCurrentContent){
            final byte[] untouchedContent = Arrays.copyOfRange(content,0, position);
            content = ArrayUtils.addAll(untouchedContent, bytesToSet);
        }else{
            content = ArrayUtils.insert(position, content, bytesToSet);
        }

        return length;
    }
}
