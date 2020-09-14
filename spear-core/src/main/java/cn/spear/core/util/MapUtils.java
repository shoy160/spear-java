package cn.spear.core.util;

import cn.spear.core.lang.Func;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Map Utils
 *
 * @author shay
 * @date 2020/9/9
 */
public class MapUtils {

    /**
     * Map Key过滤
     *
     * @param map    map
     * @param values 保留的值
     * @param <K>    Key 类型
     * @param <V>    Value 类型
     * @return 过滤之后的Map
     */
    @SafeVarargs
    public static <K, V> Map<K, V> filterValues(Map<K, V> map, V... values) {
        if (CommonUtils.isEmpty(values)) {
            return map;
        }
        return filter(map, entry -> {
            if (ArrayUtils.contains(values, entry.getValue())) {
                return entry;
            }
            return null;
        });
    }

    /**
     * Map Key过滤
     *
     * @param map  map
     * @param keys 保留的键
     * @param <K>  Key 类型
     * @param <V>  Value 类型
     * @return 过滤之后的Map
     */
    @SafeVarargs
    public static <K, V> Map<K, V> filterKeys(Map<K, V> map, K... keys) {
        if (CommonUtils.isEmpty(keys)) {
            return map;
        }
        return filter(map, entry -> {
            if (ArrayUtils.contains(keys, entry.getKey())) {
                return entry;
            }
            return null;
        });
    }

    /**
     * Map 过滤
     *
     * @param map    map
     * @param filter 过滤器，返回null则丢弃
     * @param <K>    Key 类型
     * @param <V>    Value 类型
     * @return 过滤之后的Map
     */
    public static <K, V> Map<K, V> filter(Map<K, V> map, Func<Map.Entry<K, V>, Map.Entry<K, V>> filter) {
        if (CommonUtils.isEmpty(map) || null == filter) {
            return map;
        }
        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Map.Entry<K, V> filterResult = filter.invoke(entry);
            if (filterResult != null) {
                result.put(filterResult.getKey(), filterResult.getValue());
            }
        }
        return result;
    }

    /**
     * Map 排序
     *
     * @param map map
     * @param <K> Key类型
     * @param <V> Value类型
     * @return TreeMap
     */
    public static <K, V> TreeMap<K, V> sort(Map<K, V> map) {
        return sort(map, null);
    }

    /**
     * Map 排序
     *
     * @param map        map
     * @param comparator Key值比较器
     * @param <K>        Key类型
     * @param <V>        Value类型
     * @return TreeMap
     */
    public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
        TreeMap<K, V> result = new TreeMap<>();
        if (CommonUtils.isEmpty(map)) {
            return result;
        }
        if (map instanceof TreeMap) {
            // 已经是可排序Map，此时只有比较器一致才返回原map
            result = (TreeMap<K, V>) map;
            if (null == comparator || comparator.equals(result.comparator())) {
                return result;
            }
        } else {
            if (null != comparator) {
                result = new TreeMap<>(comparator);
            }
            result.putAll(map);
        }
        return result;
    }

    /**
     * 拼接字符
     *
     * @param map               map
     * @param separator         分隔符
     * @param keyValueSeparator 键值分隔符
     * @param ignoreNull        是否忽略NULL值
     * @param otherStrings      其他字符，比如秘钥等
     * @param <K>               Key 类型
     * @param <V>               Value 类型
     * @return 拼接字符
     */
    public static <K, V> String join(Map<K, V> map, String separator, String keyValueSeparator, boolean ignoreNull, String... otherStrings) {
        return join(map, separator, keyValueSeparator, ignoreNull, null, null, otherStrings);
    }

    /**
     * 拼接字符
     *
     * @param map               map
     * @param separator         分隔符
     * @param keyValueSeparator 键值分隔符
     * @param ignoreNull        是否忽略NULL值
     * @param otherStrings      其他字符，比如秘钥等
     * @param <K>               Key 类型
     * @param <V>               Value 类型
     * @return 拼接字符
     */
    public static <K, V> String sortJoin(
            Map<K, V> map, String separator, String keyValueSeparator,
            boolean ignoreNull, String... otherStrings
    ) {
        return join(sort(map), separator, keyValueSeparator, ignoreNull, null, null, otherStrings);
    }

    /**
     * 拼接字符
     *
     * @param map               map
     * @param separator         分隔符
     * @param keyValueSeparator 键值分隔符
     * @param ignoreNull        是否忽略NULL值
     * @param <K>               Key 类型
     * @param <V>               Value 类型
     * @return 拼接字符
     */
    public static <K, V> String join(
            Map<K, V> map, String separator, String keyValueSeparator, boolean ignoreNull
    ) {
        return join(map, separator, keyValueSeparator, ignoreNull, null, null, new String[0]);
    }

    /**
     * 拼接URL参数
     *
     * @param map        map
     * @param ignoreNull 是否忽略NULL值
     * @param <K>        Key 类型
     * @param <V>        Value 类型
     * @return 拼接字符
     */
    public static <K, V> String joinUrl(Map<K, V> map, boolean ignoreNull) {
        return join(map, "&", "=", ignoreNull, null, null, new String[0]);
    }

    /**
     * 拼接URL参数
     *
     * @param map        map
     * @param ignoreNull 是否忽略NULL值
     * @param <K>        Key 类型
     * @param <V>        Value 类型
     * @return 拼接字符
     */
    public static <K, V> String sortJoinUrl(Map<K, V> map, boolean ignoreNull) {
        return join(sort(map), "&", "=", ignoreNull, null, null, new String[0]);
    }

    /**
     * 拼接URL参数并对参数值进行编码
     *
     * @param map        map
     * @param ignoreNull 是否忽略NULL值
     * @param charset    字符编码
     * @param <K>        Key 类型
     * @param <V>        Value 类型
     * @return 拼接字符
     */
    public static <K, V> String joinUrlEncode(Map<K, V> map, boolean ignoreNull, String charset) {
        return join(map, "&", "=", ignoreNull, null, value -> {
            if (null == value) {
                return "";
            }
            try {
                return URLEncoder.encode(value.toString(), charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return value.toString();
        });
    }

    /**
     * 拼接URL参数并对参数值进行编码
     *
     * @param map        map
     * @param ignoreNull 是否忽略NULL值
     * @param charset    字符编码
     * @param <K>        Key 类型
     * @param <V>        Value 类型
     * @return 拼接字符
     */
    public static <K, V> String sortJoinUrlEncode(Map<K, V> map, boolean ignoreNull, String charset) {
        return joinUrlEncode(sort(map), ignoreNull, charset);
    }

    /**
     * 拼接URL参数并对参数值进行编码(UTF-8)
     *
     * @param map        map
     * @param ignoreNull 是否忽略NULL值
     * @param <K>        Key 类型
     * @param <V>        Value 类型
     * @return 拼接字符
     */
    public static <K, V> String joinUrlEncode(Map<K, V> map, boolean ignoreNull) {
        return joinUrlEncode(map, ignoreNull, "UTF-8");
    }

    /**
     * 拼接URL参数并对参数值进行编码(UTF-8)
     *
     * @param map        map
     * @param ignoreNull 是否忽略NULL值
     * @param <K>        Key 类型
     * @param <V>        Value 类型
     * @return 拼接字符
     */
    public static <K, V> String sortJoinUrlEncode(Map<K, V> map, boolean ignoreNull) {
        return joinUrlEncode(sort(map), ignoreNull, "UTF-8");
    }

    /**
     * 拼接字符
     *
     * @param map               map
     * @param separator         分隔符
     * @param keyValueSeparator 键值分隔符
     * @param ignoreNull        是否忽略NULL值
     * @param keyFunc           Key值转换
     * @param valueFunc         Value值转换
     * @param otherStrings      其他字符，比如秘钥等
     * @param <K>               Key 类型
     * @param <V>               Value 类型
     * @return 拼接字符
     */
    public static <K, V> String join(
            Map<K, V> map, String separator, String keyValueSeparator, boolean ignoreNull,
            Func<String, K> keyFunc, Func<String, V> valueFunc, String... otherStrings
    ) {
        StringBuilder builder = new StringBuilder();
        if (CommonUtils.isNotEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                if (ignoreNull) {
                    if ((null == key || null == value)) {
                        continue;
                    }
                }
                if (null != key) {
                    if (null != keyFunc) {
                        builder.append(keyFunc.invoke(key));
                    } else {
                        builder.append(key);
                    }
                }
                //键值分隔符
                if (CommonUtils.isNotEmpty(keyValueSeparator)) {
                    builder.append(keyValueSeparator);
                }
                if (null != value) {
                    if (null != valueFunc) {
                        builder.append(valueFunc.invoke(value));
                    } else {
                        builder.append(value);
                    }
                }
                //分隔符
                if (CommonUtils.isNotEmpty(separator)) {
                    builder.append(separator);
                }
            }
            //去除多余分隔符
            if (CommonUtils.isNotEmpty(separator)) {
                builder.delete(builder.length() - separator.length(), builder.length());
            }
        }
        //拼接其他字符
        if (CommonUtils.isNotEmpty(otherStrings)) {
            for (String other : otherStrings) {
                builder.append(other);
            }
        }
        return builder.toString();
    }
}
