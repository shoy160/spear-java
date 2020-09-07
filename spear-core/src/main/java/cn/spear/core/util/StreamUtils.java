package cn.spear.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author shay
 * @date 2020/9/7
 */
public class StreamUtils {

    public static boolean isGzip(byte[] data) {
        int minLength = 2;
        if (data == null || data.length <= minLength) {
            return false;
        }
        int magic = data[0] & 0xff | ((data[1] << 8) & 0xff00);
        return magic == GZIPInputStream.GZIP_MAGIC;
    }

    /**
     * GZIP压缩
     *
     * @param data data
     * @return byte[]
     */
    public static byte[] gzip(byte[] data) {
        if (CommonUtils.isEmpty(data)) {
            return new byte[0];
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(data);
                gzip.finish();
            }
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * GZip解压
     *
     * @param data data
     * @return byte[]
     */
    public static byte[] unGzip(byte[] data) {
        if (CommonUtils.isEmpty(data)) {
            return new byte[0];
        }
        if (!isGzip(data)) {
            return data;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
                try (GZIPInputStream gzip = new GZIPInputStream(in)) {
                    byte[] buffer = new byte[256];
                    int n;
                    while ((n = gzip.read(buffer)) >= 0) {
                        out.write(buffer, 0, n);
                    }
                }
            }
            byte[] buffer = out.toByteArray();
            out.flush();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
