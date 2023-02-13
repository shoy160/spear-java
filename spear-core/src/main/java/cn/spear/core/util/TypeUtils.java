package cn.spear.core.util;

import cn.spear.core.convert.Convert;
import cn.spear.core.service.annotation.SpearService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Type Utils
 *
 * @author shay
 * @date 2020/9/8
 */
public class TypeUtils {
    public static <T> boolean isString(T t) {
        return t instanceof String;
    }

    public static boolean isString(Class<?> clazz) {
        return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
    }

    public static <T> boolean isByte(T t) {
        return t instanceof Byte;
    }

    public static boolean isByte(Class<?> clazz) {
        return Byte.class.equals(clazz);
    }

    public static <T> boolean isShort(T t) {
        return t instanceof Short;
    }

    public static boolean isShort(Class<?> clazz) {
        return Short.class.equals(clazz);
    }

    public static <T> boolean isInt(T t) {
        return t instanceof Integer;
    }

    public static boolean isInt(Class<?> clazz) {
        return Integer.class.equals(clazz);
    }

    public static <T> boolean isLong(T t) {
        return t instanceof Long;
    }

    public static boolean isLong(Class<?> clazz) {
        return Long.class.equals(clazz);
    }

    public static <T> boolean isChar(T t) {
        return t instanceof Character;
    }

    public static boolean isChar(Class<?> clazz) {
        return Character.class.equals(clazz);
    }

    public static <T> boolean isFloat(T t) {
        return t instanceof Float;
    }

    public static boolean isFloat(Class<?> clazz) {
        return Float.class.equals(clazz);
    }

    public static <T> boolean isDouble(T t) {
        return t instanceof Double;
    }

    public static boolean isDouble(Class<?> clazz) {
        return Double.class.equals(clazz);
    }

    public static <T> boolean isBytes(T t) {
        return t instanceof Byte;
    }

    public static boolean isBytes(Class<?> clazz) {
        return Byte.class.equals(clazz);
    }

    public static <T> boolean isDate(T t) {
        return t instanceof Date;
    }

    public static boolean isDate(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz) || LocalDateTime.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是简单类型
     *
     * @param t   instance
     * @param <T> T
     * @return boolean
     */
    public static <T> boolean isSimple(T t) {
        return isString(t) || isInt(t) || isByte(t) || isShort(t)
                || isLong(t) || isChar(t) || isFloat(t)
                || isDouble(t) || isDate(t) || isBytes(t);
    }

    /**
     * 是否是简单类型
     *
     * @param clazz class
     * @return boolean
     */
    public static boolean isSimple(Class<?> clazz) {
        return isString(clazz) || isInt(clazz) || isByte(clazz)
                || isShort(clazz) || isLong(clazz) || isChar(clazz)
                || isFloat(clazz) || isDouble(clazz) || isDate(clazz) || isBytes(clazz);
    }

    public static <T> Class<?> getClassType(T t) {
        return t.getClass();
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isNumber(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz);
    }

    public static boolean isBoolean(Class<?> clazz) {
        return clazz.equals(Boolean.class);
    }

    public static ParameterizedType toParameterizedType(Type type) {
        ParameterizedType result = null;
        if (type instanceof ParameterizedType) {
            result = (ParameterizedType) type;
        } else if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            Type genericSuper = clazz.getGenericSuperclass();
            if (null == genericSuper || Object.class.equals(genericSuper)) {
                Type[] genericInterfaces = clazz.getGenericInterfaces();
                if (genericInterfaces.length > 0) {
                    genericSuper = genericInterfaces[0];
                }
            }

            result = toParameterizedType(genericSuper);
        }
        return result;
    }

    public static <T> Class<T> getGenericClass(Type type, int index) {
        try {
            ParameterizedType parameterizedType = toParameterizedType(type);
            if (parameterizedType == null) {
                return null;
            }
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (CommonUtils.isEmpty(typeArguments) || typeArguments.length <= index) {
                return null;
            }
            Type resultType = typeArguments[index];
            return Convert.convert(resultType);
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

    private static final Map<Class<?>, String> SERVICE_NAME_CACHE = new HashMap<>();

    public static String getServiceName(Class<?> serviceClazz) {
        if (SERVICE_NAME_CACHE.containsKey(serviceClazz)) {
            return SERVICE_NAME_CACHE.get(serviceClazz);
        }
        String name;
        SpearService service = serviceClazz.getAnnotation(SpearService.class);
        if (null == service || CommonUtils.isEmpty(service.service())) {
            name = serviceClazz.getName();
        } else {
            name = service.service();
        }
        SERVICE_NAME_CACHE.put(serviceClazz, name);
        return name;
    }
}
