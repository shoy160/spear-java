package cn.spear.core.convert;

import cn.spear.core.Constants;

import java.util.Calendar;
import java.util.Date;

/**
 * @author shoy
 * @date 2021/9/28
 */
public class Convert {

    /**
     * Convert
     *
     * @param value value
     * @param clazz class
     * @param <T>   T
     * @return result
     */
    public static <T> T convert(Object value, Class<T> clazz) {
        if (null == value) {
            return null;
        }
        return clazz.cast(value);
    }

    /**
     * Convert
     *
     * @param value value
     * @param <T>   T
     * @return result
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object value) {
        if (null == value) {
            return null;
        }
        return (T) value;
    }

    /**
     * 转换为long<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        }
        if (value instanceof Date) {
            return (int) ((Date) value).getTime() / 1000;
        }
        if (value instanceof Calendar) {
            return (int) ((Calendar) value).getTimeInMillis() / 1000;
        }
        try {
            String strValue = value.toString();
            if (strValue.startsWith(Constants.RADIX_HEX)) {
                // 0x表示16进制数
                return Integer.parseInt(strValue.substring(2), 16);
            }
            return Integer.parseInt(strValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * int
     * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Integer toInt(Object value) {
        return toInt(value, null);
    }

    /**
     * 转换为long<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Long toLong(Object value, Long defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? 1L : 0L;
        }
        if (value instanceof Date) {
            return ((Date) value).getTime();
        }
        if (value instanceof Calendar) {
            return ((Calendar) value).getTimeInMillis();
        }
        try {
            String strValue = value.toString();
            if (strValue.startsWith(Constants.RADIX_HEX)) {
                // 0x表示16进制数
                return Long.parseLong(strValue.substring(2), 16);
            }
            return Long.parseLong(strValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 转换为long<br>
     * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Long toLong(Object value) {
        return toLong(value, null);
    }
}
