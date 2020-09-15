package cn.spear.core.ioc;

import cn.spear.core.ioc.impl.ServiceProviderImpl;
import cn.spear.core.lang.Func;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

/**
 * @author shay
 * @date 2020/9/8
 */
public interface ServiceCollection {

    /**
     * 添加服务
     *
     * @param descriptor descriptor
     * @return services
     */
    ServiceCollection add(ServiceDescriptor descriptor);

    /**
     * 获取服务描述
     *
     * @return list
     */
    List<ServiceDescriptor> getDescriptors();

    /**
     * 添加IOC
     *
     * @param serviceType        serviceType
     * @param implementationType implementationType
     * @param lifetime           lifetime
     * @return Services
     */
    default ServiceCollection add(Type serviceType, Type implementationType, ServiceLifetime lifetime) {
        ServiceDescriptor descriptor = ServiceDescriptor.describe(serviceType, implementationType, lifetime);
        add(descriptor);
        return this;
    }

    /**
     * 添加IOC
     *
     * @param serviceType serviceType
     * @param factory     factory
     * @param lifetime    lifetime
     * @return services
     */
    default ServiceCollection add(Type serviceType, Func<Object, ServiceProvider> factory, ServiceLifetime lifetime) {
        ServiceDescriptor descriptor = ServiceDescriptor.describe(serviceType, factory, lifetime);
        add(descriptor);
        return this;
    }

    /**
     * add IOC for Transient
     *
     * @param serviceType        serviceType
     * @param implementationType implementationType
     * @return services
     */
    default ServiceCollection addTransient(Type serviceType, Type implementationType) {
        return add(serviceType, implementationType, ServiceLifetime.Transient);
    }

    /**
     * add IOC for Transient
     *
     * @param serviceType serviceType
     * @param factory     factory
     * @return services
     */
    default ServiceCollection addTransient(Type serviceType, Func<Object, ServiceProvider> factory) {
        return add(serviceType, factory, ServiceLifetime.Transient);
    }

    /**
     * add IOC for Scoped
     *
     * @param serviceType        serviceType
     * @param implementationType implementationType
     * @return services
     */
    default ServiceCollection addScoped(Type serviceType, Type implementationType) {
        return add(serviceType, implementationType, ServiceLifetime.Scoped);
    }

    /**
     * add IOC for Scoped
     *
     * @param serviceType serviceType
     * @param factory     factory
     * @return services
     */
    default ServiceCollection addScoped(Type serviceType, Func<Object, ServiceProvider> factory) {
        return add(serviceType, factory, ServiceLifetime.Scoped);
    }

    /**
     * add IOC for Singleton
     *
     * @param serviceType        serviceType
     * @param implementationType implementationType
     * @return services
     */
    default ServiceCollection addSingleton(Type serviceType, Type implementationType) {
        return add(serviceType, implementationType, ServiceLifetime.Singleton);
    }

    /**
     * add IOC for Singleton
     *
     * @param serviceType serviceType
     * @param factory     factory
     * @return services
     */
    default ServiceCollection addSingleton(Type serviceType, Func<Object, ServiceProvider> factory) {
        return add(serviceType, factory, ServiceLifetime.Singleton);
    }

    /**
     * 构建服务
     *
     * @return provider
     */
    default ServiceProvider build() {
        return IocContext.provider = new ServiceProviderImpl(this.getDescriptors());
    }
}
