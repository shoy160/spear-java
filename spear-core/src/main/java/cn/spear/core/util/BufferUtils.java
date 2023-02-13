package cn.spear.core.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Stream Utils
 *
 * @author shay
 * @date 2020/9/7
 */
@Slf4j
public class BufferUtils {

    private final static int LENGTH_INT = 4;
    private final static int LENGTH_LONG = 8;

    /**
     * 是否为Gzip压缩
     *
     * @param data byte[]
     * @return boolean
     */
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
        if (log.isDebugEnabled()) {
            log.debug("before gzip:{}", data.length);
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(data);
                gzip.finish();
            }
            byte[] buffer = out.toByteArray();
            if (log.isDebugEnabled()) {
                log.debug("after gzip:{}", buffer.length);
            }
            return buffer;
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

    public static byte[] combine(byte[] data, byte[] other) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length + other.length);
        buffer.put(data);
        buffer.put(other);
        return buffer.array();
    }

    /**
     * 转字符
     *
     * @param bytes   bytes
     * @param charset charset
     * @return String
     */
    public static String toString(byte[] bytes, Charset charset) {
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        return new String(bytes, charset);
    }

    public static int toInt(byte[] bytes) {
        int value = 0;
        int length = bytes.length;
        if (length > LENGTH_INT) {
            //防止溢出
            length = LENGTH_INT;
        }
        for (int i = length - 1; i >= 0; i--) {
            value += (bytes[i] & 0xFF) << (length - i - 1) * 8;
        }
        return value;
    }

    public static long toLong(byte[] bytes) {
        return new BigInteger(bytes).longValue();
    }

    public static String toHex(byte[] bytes) {
        return toHex(bytes, " ", false);
    }

    public static String toHex(byte[] bytes, String delimiter, boolean lowerCase) {
        String strHex;
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            strHex = Integer.toHexString(aByte & 0xFF);
            strHex = strHex.length() == 1 ? "0".concat(strHex) : strHex;
            sb.append(strHex);
            if (StringUtils.isNotEmpty(delimiter)) {
                sb.append(delimiter);
            }
        }
        strHex = sb.toString();
        if (StringUtils.isNotEmpty(delimiter)) {
            strHex = strHex.substring(0, strHex.length() - delimiter.length());
        }
        return lowerCase ? strHex.toLowerCase() : strHex.toUpperCase();
    }

    private static byte[] sample(byte[] bytes) {
        int start = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] > 0) {
                start = i;
                break;
            }
        }
        return start > 0 ? Arrays.copyOfRange(bytes, start, bytes.length) : bytes;
    }

    public static byte[] fromInt(int value, boolean sample) {
        byte[] result = new byte[LENGTH_INT];
        for (int i = LENGTH_INT - 1; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return sample ? sample(result) : result;
    }

    public static byte[] fromLong(long value, boolean sample) {
        byte[] result = new byte[LENGTH_LONG];
        for (int i = LENGTH_LONG - 1; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return sample ? sample(result) : result;
    }

    public static byte[] fromString(String msg) {
        byte[] bytes = new byte[msg.length()];
        final char[] chars = msg.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }
}
