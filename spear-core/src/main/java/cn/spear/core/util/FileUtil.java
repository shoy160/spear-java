package cn.spear.core.util;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author shoy
 * @date 2021/9/28
 */
public final class FileUtil {

    /**
     * 字符常量：斜杠 {@code '/'}
     */
    public static final char SLASH = '/';
    /**
     * 字符常量：反斜杠 {@code '\\'}
     */
    public static final char BACKSLASH = '\\';

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }

    /**
     * 返回文件名
     *
     * @param filePath 文件
     * @return 文件名
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    /**
     * 获得文件的MimeType
     *
     * @param file 文件
     * @return MimeType
     * @see Files#probeContentType(Path)
     */
    public static String getMimeType(Path file) {
        try {
            return Files.probeContentType(file);
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * 根据文件扩展名获得MimeType
     *
     * @param filePath 文件路径或文件名
     * @return MimeType
     */
    public static String getMimeType(String filePath) {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(filePath);
        if (null == contentType) {
            // 补充一些常用的mimeType
            if (filePath.endsWith(".css")) {
                contentType = "text/css";
            } else if (filePath.endsWith(".js")) {
                contentType = "application/x-javascript";
            }
        }

        // 补充
        if (null == contentType) {
            contentType = getMimeType(Paths.get(filePath));
        }

        return contentType;
    }


    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过Integer.MAX_VALUE
     *
     * @param filePath 文件路径
     * @return 字节码
     */
    public static byte[] readBytes(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
