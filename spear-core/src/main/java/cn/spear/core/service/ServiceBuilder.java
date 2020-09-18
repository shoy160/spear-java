package cn.spear.core.service;

import cn.spear.core.ioc.ServiceCollection;
import cn.spear.core.ioc.ServiceDescriptor;
import cn.spear.core.lang.Action;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.proxy.ProxyFactory;
import cn.spear.core.proxy.impl.DefaultProxyFactory;
import cn.spear.core.service.enums.ServiceProtocol;
import cn.spear.core.service.impl.DefaultServiceEntryFactory;
import cn.spear.core.service.impl.DefaultServiceExecutor;
import cn.spear.core.service.impl.DefaultServiceGenerator;
import cn.spear.core.service.impl.DefaultServiceHost;

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
     * @param <T>            T
     * @param routerInstance routeType
     * @return builder
     */
    default <T extends ServiceRegister & ServiceFinder> ServiceBuilder addRoute(T routerInstance) {
        add(new ServiceDescriptor(ServiceRegister.class, routerInstance));
        add(new ServiceDescriptor(ServiceFinder.class, routerInstance));
        return this;
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
    default ServiceBuilder addServer(Action<ServiceBuilder> action) {
        return addServer(action, "");
    }

    /**
     * 添加Spear服务
     *
     * @param action      action
     * @param basePackage 基础包
     * @return builder
     */
    default ServiceBuilder addServer(Action<ServiceBuilder> action, String basePackage) {
        addSingleton(ServiceGenerator.class, DefaultServiceGenerator.class);
        addSingleton(ServiceEntryFactory.class, p -> new DefaultServiceEntryFactory(this, basePackage));
        action.invoke(this);
        addSingleton(ServiceExecutor.class, provider -> {
            ServiceEntryFactory entryFactory = provider.getServiceT(ServiceEntryFactory.class);
            return new DefaultServiceExecutor(entryFactory);
        });
        addSingleton(ServiceHost.class, provider -> {
            ServiceExecutor executor = provider.getServiceT(ServiceExecutor.class);
            ServiceRegister register = provider.getServiceT(ServiceRegister.class);
            ServiceEntryFactory entryFactory = provider.getServiceT(ServiceEntryFactory.class);
            ServiceListener listener = provider.getServiceT(ServiceListener.class);
            return new DefaultServiceHost(executor, register, entryFactory, listener);
        });
        return this;
    }

    /**
     * 添加Spear客户端
     *
     * @param action action
     * @return builder
     */
    default ServiceBuilder addClient(Action<ServiceBuilder> action) {
        addSingleton(ServiceGenerator.class, DefaultServiceGenerator.class);
        addSingleton(ProxyFactory.class, DefaultProxyFactory.class);
        action.invoke(this);
        return this;
    }
}
