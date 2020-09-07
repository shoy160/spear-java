package cn.spear.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author shay
 * @date 2020/9/4
 */
public class CommonUtils {

    public static boolean isEmpty(Object object) {
        if (null == object) {
            return true;
        }
        if (object instanceof CharSequence) {
            return 0 == ((CharSequence) object).length();
        }
        if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        }
        if (object instanceof Iterable) {
            Iterator<?> iterator = ((Iterable<?>) object).iterator();
            return !iterator.hasNext();
        }
        if (object instanceof Iterator) {
            return !((Iterator<?>) object).hasNext();
        }
        if (object.getClass().isArray()) {
            return 0 == Array.getLength(object);
        }
        return false;
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static String fastId() {
        return UUID.randomUUID().toString();
    }

    public static <T> Class<T> getGenericClass(Class<?> parentClazz, int index) {
        try {
            ParameterizedType genericSuperclass = (ParameterizedType) parentClazz.getGenericSuperclass();
            if (genericSuperclass == null) {
                return null;
            }
            Type[] typeArguments = genericSuperclass.getActualTypeArguments();
            if (isEmpty(typeArguments) || typeArguments.length <= index) {
                return null;
            }
            return (Class<T>) typeArguments[index];
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T> T createGenericInstance(Class<?> parentClazz) {
        return createGenericInstance(parentClazz, 0);
    }

    public static <T> T createGenericInstance(Class<?> parentClazz, int index) {
        Class<T> clazz = getGenericClass(parentClazz, index);
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
