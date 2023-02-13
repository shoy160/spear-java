package cn.spear.core.ioc;

import cn.spear.core.lang.Action;

import java.lang.reflect.Type;

/**
 * @author shay
 * @date 2020/9/8
 */
public interface ServiceProvider {
    /**
     * 获取服务
     *
     * @param serviceType serviceType
     * @return Object
     */
    Object getService(Type serviceType);

    /**
     * 开始生命周期
     */
    default void startScope() {
        completeScope();
    }

    /**
     * 完成生命周期
     */
    void completeScope();

    /**
     * 运行周期
     *
     * @param action action
     */
    default void scope(Action<ServiceProvider> action) {
        startScope();
        action.invoke(this);
        completeScope();
    }

    /**
     * 获取服务
     *
     * @param serviceType serviceType
     * @param <T>         T
     * @return T
     */
    default <T> T getServiceT(Class<T> serviceType) {
        Object instance = getService(serviceType);
        if (null == instance) {
            return null;
        }
        return serviceType.cast(instance);
    }
}
