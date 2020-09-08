package cn.spear.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Type Utils
 *
 * @author shay
 * @date 2020/9/8
 */
public class TypeUtils {
    public static ParameterizedType toParameterizedType(Type type) {
        ParameterizedType result = null;
        if (type instanceof ParameterizedType) {
            result = (ParameterizedType) type;
        } else if (type instanceof Class) {
            Class<?> clazz = (Class) type;
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
            return (Class<T>) resultType;
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
