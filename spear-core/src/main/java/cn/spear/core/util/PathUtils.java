package cn.spear.core.util;

/**
 * @author shay
 * @date 2020/9/18
 */
public class PathUtils {

    private final static String PATH_REG = "\\\\";

    /**
     * 获取名称,包含扩展名
     *
     * @param path 路径
     * @return name
     */
    public static String getName(String path) {
        if (CommonUtils.isEmpty(path)) {
            return "";
        }
        path = path.replaceAll(PATH_REG, "/");
        int begin = path.lastIndexOf("/");
        begin = Math.max(begin + 1, 0);
        return path.substring(begin);
    }

    /**
     * 获取扩展名
     *
     * @param path 路径
     * @return 扩展名(不含.)
     */
    public static String getExt(String path) {
        if (CommonUtils.isEmpty(path)) {
            return "";
        }
        path = path.replaceAll(PATH_REG, "/");
        int end = path.lastIndexOf(".");
        if (end <= 0) {
            end = path.length();
        }
        return path.substring(end + 1);
    }

    /**
     * 获取名称，不包含扩展名
     *
     * @param path 路径
     * @return 名称
     */
    public static String getMainName(String path) {
        if (CommonUtils.isEmpty(path)) {
            return "";
        }
        path = path.replaceAll(PATH_REG, "/");
        int begin = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        begin = Math.max(begin + 1, 0);
        if (end <= 0) {
            end = path.length();
        }
        return path.substring(begin, end);
    }
}
