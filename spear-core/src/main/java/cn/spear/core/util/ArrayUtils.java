package cn.spear.core.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author shay
 * @date 2020/9/9
 */
public class ArrayUtils {
    public static final int INDEX_NOT_FOUND = -1;

    public static <T> int indexOf(T[] array, Object value) {
        if (null != array) {
            for (int i = 0; i < array.length; i++) {
                if (Objects.equals(value, array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static <T> int lastIndexOf(T[] array, Object value) {
        if (null != array) {
            for (int i = array.length - 1; i >= 0; i--) {
                if (Objects.equals(value, array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) > INDEX_NOT_FOUND;
    }

    @SafeVarargs
    public static <T> boolean containsAny(T[] array, T... values) {
        if (CommonUtils.isEmpty(values)) {
            return true;
        }
        for (T value : values) {
            if (contains(array, value)) {
                return true;
            }
        }
        return false;
    }
}
