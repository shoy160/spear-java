package cn.spear.core.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

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

    public static <T> T cast(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }
}
