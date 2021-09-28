package cn.spear.core.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author shoy
 * @date 2021/9/28
 */
public final class UrlUtil {

    /**
     * 补全相对路径
     *
     * @param baseUrl      基准URL
     * @param relativePath 相对URL
     * @return 相对路径
     */
    public static String completeUrl(String baseUrl, String relativePath) {
        if (CommonUtils.isEmpty(baseUrl)) {
            return null;
        }

        try {
            final URL absoluteUrl = new URL(baseUrl);
            final URL parseUrl = new URL(absoluteUrl, relativePath);
            return parseUrl.toString();
        } catch (MalformedURLException e) {
            return baseUrl + relativePath;
        }
    }
}
