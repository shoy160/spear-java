package cn.spear.core.proxy.http;

import cn.spear.core.Singleton;
import cn.spear.core.proxy.ProxyFactory;
import cn.spear.core.proxy.http.handler.HttpProxyHandler;

/**
 * @author shay
 * @date 2021/3/18
 */
public interface HttpClientFactory extends ProxyFactory {

    /**
     * 创建代理
     *
     * @param clazz clazz
     * @return instance
     */
    @Override
    default Object create(Class<?> clazz) {
        return create(clazz, new HttpProxyHandler(-1, null));
    }

    /**
     * 创建代理
     *
     * @param clazz  clazz
     * @param filter filter
     * @return instance
     */
    default Object create(Class<?> clazz, HttpClientFilter filter) {
        return create(clazz, new HttpProxyHandler(-1, filter));
    }

    /**
     * 创建代理
     *
     * @param clazz class
     * @param <T>   T
     * @return T
     */
    @Override
    default <T> T createT(Class<T> clazz) {
        return createT(clazz, new HttpProxyHandler(-1, null));
    }

    /**
     * 创建代理
     *
     * @param clazz  class
     * @param filter filter
     * @param <T>    T
     * @return T
     */
    default <T> T createT(Class<T> clazz, HttpClientFilter filter) {
        return createT(clazz, new HttpProxyHandler(-1, filter));
    }


    /**
     * 创建代理
     *
     * @param clazz   class
     * @param timeout 超时时间(秒)
     * @param <T>     T
     * @return T
     */
    default <T> T createT(Class<T> clazz, Integer timeout) {
        return createT(clazz, new HttpProxyHandler(timeout, null));
    }

    /**
     * 创建代理
     *
     * @param clazz   class
     * @param timeout 超时时间(秒)
     * @param filter  filter
     * @param <T>     T
     * @return T
     */
    default <T> T createT(Class<T> clazz, Integer timeout, HttpClientFilter filter) {
        return createT(clazz, new HttpProxyHandler(timeout, filter));
    }

    /**
     * 获取默认实例
     *
     * @return instance
     */
    static HttpClientFactory instance() {
        return Singleton.instance((clazz, handler) -> ProxyFactory.instance().create(clazz, handler));
    }
}
