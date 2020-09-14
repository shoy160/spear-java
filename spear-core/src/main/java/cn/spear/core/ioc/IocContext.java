package cn.spear.core.ioc;

import java.lang.reflect.Type;

/**
 * @author shay
 * @date 2020/9/14
 */
public class IocContext {
    public static ServiceProvider provider;

    public static Object getService(Type type) {
        return provider.getService(type);
    }

    public static <T> T getServiceT(Class<T> clazz) {
        return provider.getServiceT(clazz);
    }
}
