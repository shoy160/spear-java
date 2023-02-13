package cn.spear.core.ioc;

import cn.spear.core.lang.Action;

import java.lang.reflect.Type;

/**
 * @author shay
 * @date 2020/9/14
 */
public class IocContext {
    private static ServiceProvider provider;

    public static void setProvider(ServiceProvider provider) {
        IocContext.provider = provider;
    }

    public static void scope(Action<ServiceProvider> action) {
        if (null == action || null == provider) {
            return;
        }
        provider.scope(action);
    }

    public static Object getService(Type type) {
        if (null == provider) {
            return null;
        }
        return provider.getService(type);
    }

    public static <T> T getServiceT(Class<T> clazz) {
        if (null == provider) {
            return null;
        }
        return provider.getServiceT(clazz);
    }
}
