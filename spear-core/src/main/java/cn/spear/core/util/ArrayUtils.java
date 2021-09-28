package cn.spear.core.util;

import cn.spear.core.lang.Weight;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

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

    private static <T> void swap(T[] a, int i, int j) {
        T temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static <T> void randomSort(T[] arr) {
        if (CommonUtils.isEmpty(arr) || arr.length <= 1) {
            return;
        }
        int length = arr.length;
        Random rand = new Random();
        for (int i = length; i > 0; i--) {
            int randInd = rand.nextInt(i);
            swap(arr, randInd, i - 1);
        }
    }

    public static <T> T random(T[] arr) {
        if (CommonUtils.isEmpty(arr)) {
            return null;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        randomSort(arr);
        return arr[0];
    }

    /**
     * 权重随机
     *
     * @param iterable list
     * @param <T>      T
     * @return T
     */
    public static <T extends Weight> T randomWeight(Iterable<T> iterable) {
        if (CommonUtils.isEmpty(iterable)) {
            return null;
        }
        int total = 0;
        for (T t : iterable) {
            total += t.getWeight();
        }
        int randomIdx = RandomUtils.randomInt(total);
        total = 0;
        for (T t : iterable) {
            total += t.getWeight();
            if (total > randomIdx) {
                return t;
            }
        }
        return null;
    }

    /**
     * 权重随机
     *
     * @param array array
     * @param <T>   T
     * @return T
     */
    public static <T extends Weight> T randomWeight(T[] array) {
        int total = 0;
        for (T t : array) {
            total += t.getWeight();
        }
        int randomIdx = RandomUtils.randomInt(total);
        total = 0;
        for (T t : array) {
            total += t.getWeight();
            if (total > randomIdx) {
                return t;
            }
        }
        return random(array);
    }

    /**
     * 新建一个空数组
     *
     * @param <T>           数组元素类型
     * @param componentType 元素类型
     * @param newSize       大小
     * @return 空数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<?> componentType, int newSize) {
        return (T[]) Array.newInstance(componentType, newSize);
    }


    /**
     * 将集合转为数组
     *
     * @param <T>        数组元素类型
     * @param collection 集合
     * @param clazz      集合元素类型
     * @return 数组
     * @since 3.0.9
     */
    public static <T> T[] toArray(Collection<T> collection, Class<T> clazz) {
        return collection.toArray(newArray(clazz, 0));
    }
}
