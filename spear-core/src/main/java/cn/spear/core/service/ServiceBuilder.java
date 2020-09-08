package cn.spear.core.service;

import cn.spear.core.ioc.ServiceCollection;
import cn.spear.core.lang.Action;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.service.enums.ServiceProtocol;
import cn.spear.core.service.impl.ServiceEntryFactoryImpl;
import cn.spear.core.service.impl.ServiceExecutorImpl;
import cn.spear.core.service.impl.ServiceHostImpl;

/**
 * @author shay
 * @date 2020/9/8
 */
public interface ServiceBuilder extends ServiceCollection {

    /**
     * 添加编解码器
     *
     * @param <T>   T
     * @param clazz clazz
     * @return builder
     */
    <T extends MessageCodec> ServiceBuilder addCodec(Class<T> clazz);

    /**
     * 添加路由
     *
     * @param <T>       T
     * @param routeType routeType
     * @return builder
     */
    default <T extends ServiceRegister & ServiceFinder> ServiceBuilder addRoute(Class<T> routeType) {
        return addRoute(routeType, routeType);
    }

    /**
     * 添加路由
     *
     * @param <TRegister>  registerType
     * @param <TFinder>    finderType
     * @param registerType registerType
     * @param finderType   finderType
     * @return builder
     */
    <TRegister extends ServiceRegister, TFinder extends ServiceFinder> ServiceBuilder addRoute(Class<TRegister> registerType, Class<TFinder> finderType);

    /**
     * 添加协议
     *
     * @param protocol protocol
     * @return builder
     */
    ServiceBuilder addProtocol(ServiceProtocol protocol);

    /**
     * 添加Spear服务
     *
     * @param action action
     * @return builder
     */
    default ServiceBuilder addSpearServer(Action<ServiceBuilder> action) {
        addSingleton(ServiceEntryFactory.class, ServiceEntryFactoryImpl.class);
        action.invoke(this);
        addSingleton(ServiceExecutor.class, provider -> {
            ServiceEntryFactory entryFactory = provider.getServiceT(ServiceEntryFactory.class);
            return new ServiceExecutorImpl(entryFactory);
        });
        addSingleton(ServiceHost.class, provider -> {
            ServiceExecutor executor = provider.getServiceT(ServiceExecutor.class);
            ServiceRegister register = provider.getServiceT(ServiceRegister.class);
            ServiceEntryFactory entryFactory = provider.getServiceT(ServiceEntryFactory.class);
            ServiceListener listener = provider.getServiceT(ServiceListener.class);
            return new ServiceHostImpl(executor, register, entryFactory, listener);
        });
        return this;
    }

    /**
     * 添加Spear客户端
     *
     * @return builder
     */
    default ServiceBuilder addSpearClient() {
        return this;
    }
}
