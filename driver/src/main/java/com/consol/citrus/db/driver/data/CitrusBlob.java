package com.consol.citrus.db.driver.data;

import com.consol.citrus.db.driver.utils.LobUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;

public class CitrusBlob implements Blob {

    private byte[] content = ArrayUtils.EMPTY_BYTE_ARRAY;
    private final LobUtils lobUtils = new LobUtils();

    @Override
    public long length() {
        return content.length;
    }

    @Override
    public byte[] getBytes(final long pos, final int length) {
        final long positionWithOffset = lobUtils.applyOffset(pos);
        if(lobUtils.fitsInInt(positionWithOffset)){
            final int offsetPositionInInt = (int) positionWithOffset;
            return ArrayUtils.subarray(content, offsetPositionInInt, offsetPositionInInt + length);
        }
        return new byte[length];
    }

    @Override
    public InputStream getBinaryStream() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public long position(final byte[] pattern, final long start) {
        if(lobUtils.fitsInInt(start)){
            final int intStart = (int)start;
            return findPositionInBlobContent(pattern, intStart);
        }
        return -1;
    }

    @Override
    public long position(final Blob pattern, final long start) throws SQLException {
        if(lobUtils.fitsInInt(start)) {
            final int intStart = (int) start;
            try {
                return findPositionInBlobContent(IOUtils.toByteArray(pattern.getBinaryStream()), intStart);
            } catch (final IOException e) {
                throw new SQLException(e);
            }
        }
        return -1;
    }

    @Override
    public OutputStream setBinaryStream(final long pos) {
        if(lobUtils.fitsInInt(pos)){
            return new OutputStream() {
                int index = (int) lobUtils.applyOffset(pos);

                @Override
                public void write(final int byteToWrite) {
                    content[index++] = (byte) byteToWrite;
                }
            };
        }
        return null;
    }

    @Override
    public int setBytes(final long pos, final byte[] bytes) {
        final long positionWithOffset = lobUtils.applyOffset(pos);
        if(lobUtils.fitsInInt(positionWithOffset)){
            return setContent((int)positionWithOffset, bytes, 0, bytes.length);
        }

        return 0;
    }

    @Override
    public int setBytes(final long pos, final byte[] bytes, final int offset, final int len) {
        final long positionWithOffset = lobUtils.applyOffset(pos);
        if(lobUtils.fitsInInt(positionWithOffset)){
            return setContent((int)positionWithOffset, bytes, offset, len);
        }

        return 0;
    }

    @Override
    public void truncate(final long len) {
        if(lobUtils.fitsInInt(len)){
            content = ArrayUtils.subarray(content, 0, (int)len);
        }
    }

    @Override
    public void free() {
        content = ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    @Override
    public InputStream getBinaryStream(final long pos, final long length) {
        if(lobUtils.fitsInInt(pos) && lobUtils.fitsInInt(length)){
            final int intPosWithOffset = (int) lobUtils.applyOffset(pos);
            final int endIndex = intPosWithOffset + (int) length;
            final byte[] subarray = ArrayUtils.subarray(content, intPosWithOffset, endIndex);
            return new ByteArrayInputStream(subarray);
        }
        return new ByteArrayInputStream(ArrayUtils.EMPTY_BYTE_ARRAY);
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
        return Base64.encodeBase64String(content);
    }

    private long findPositionInBlobContent(final byte[] pattern, final int start) {
        for(int position = start - 1; position < content.length;  position++){
            if(content[position] == pattern[0]){
                final byte[] subarray = ArrayUtils.subarray(content, position, position + pattern.length);
                if(Arrays.equals(subarray, pattern)){
                    return (long) position + 1;
                }
            }
        }
        return -1;
    }

    /**
     * Alters the content of this @{@link CitrusBlob} to contain the given bytes.
     * If the size of the altered bytes exceeds the current capacity, the capacity is automatically extended to the
     * required size.
     * @param position The start position for altering the content. Starts at 0.
     * @param bytesToSet The bytes to set the content from.
     * @param offset  the index of the first character of {@code bytesToSet} to be inserted. Starting at 0.
     * @param length The number of bytes to set.
     * @return The number of the bytes that have been set
     */
    private int setContent(final int position,
                           final byte[] bytesToSet,
                           final int offset,
                           final int length) {
        final boolean expandsCurrentContent = position + length > content.length;
        final byte[] limitedBytesToSet = ArrayUtils.subarray(bytesToSet, offset, offset+length);
        final byte[] bytesBeforeAddingContent = Arrays.copyOfRange(content, 0, position);

        if(expandsCurrentContent) {
            content = ArrayUtils.addAll(bytesBeforeAddingContent, limitedBytesToSet);
        }else{
            final byte[] bytesAfterReplacement = Arrays.copyOfRange(content, position + length, content.length);
            final byte[] bytesIncludingReplacement = ArrayUtils.addAll(bytesBeforeAddingContent, limitedBytesToSet);
            content = ArrayUtils.addAll(bytesIncludingReplacement, bytesAfterReplacement);
        }

        return length;
    }
}
