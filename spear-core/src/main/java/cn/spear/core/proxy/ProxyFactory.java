package cn.spear.core.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * 代理工厂
 *
 * @author shay
 * @date 2020/9/15
 */
public interface ProxyFactory {
    /**
     * 创建代理
     *
     * @param clazz   类型
     * @param handler handler
     * @return 实例
     */
    Object create(Class<?> clazz, InvocationHandler handler);

    /**
     * 创建代理
     *
     * @param clazz clazz
     * @return instance
     */
    default Object create(Class<?> clazz) {
        return create(clazz, new ClientProxyHandler(-1));
    }

    /**
     * 创建代理
     *
     * @param clazz class
     * @param <T>   T
     * @return T
     */
    default <T> T createT(Class<T> clazz) {
        return createT(clazz, new ClientProxyHandler(-1));
    }

    /**
     * 创建代理
     *
     * @param clazz   class
     * @param timeout 超时时间(秒)
     * @param <T>     T
     * @return T
     */
    default <T> T createT(Class<T> clazz, long timeout) {
        return createT(clazz, new ClientProxyHandler(timeout));
    }

    /**
     * 创建代理
     *
     * @param clazz   class
     * @param handler handler
     * @param <T>     T
     * @return T
     */
    default <T> T createT(Class<T> clazz, InvocationHandler handler) {
        Object instance = create(clazz, handler);
        if (instance == null) {
            return null;
        }
        return (T) instance;
    }
}
