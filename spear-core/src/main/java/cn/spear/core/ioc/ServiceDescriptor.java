package cn.spear.core.ioc;

import cn.spear.core.lang.Func;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.TypeUtils;
import com.sun.corba.se.spi.ior.ObjectKey;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * @author shay
 * @date 2020/9/8
 */
@Getter
public class ServiceDescriptor {

    private final Type serviceType;
    private Type implementationType;
    private final ServiceLifetime lifetime;
    private Object implementationInstance;
    private Func<Object, ServiceProvider> implementationFactory;

    public ServiceDescriptor(Type serviceType, ServiceLifetime lifetime) {
        this.serviceType = serviceType;
        this.lifetime = lifetime;
    }

    public ServiceDescriptor(Type serviceType, Type implementationType, ServiceLifetime lifetime) {
        this(serviceType, lifetime);
        this.implementationType = implementationType;
    }

    public ServiceDescriptor(Type serviceType, ObjectKey instance) {
        this(serviceType, ServiceLifetime.Singleton);
        this.implementationInstance = instance;
    }

    public ServiceDescriptor(Type serviceType, Func<Object, ServiceProvider> factory, ServiceLifetime lifetime) {
        this(serviceType, lifetime);
        this.implementationFactory = factory;
    }

    /**
     * 获取实现类型
     *
     * @return Type
     */
    public Type getImplementationType() {
        if (null != this.implementationType) {
            return this.implementationType;
        } else if (null != this.implementationInstance) {
            return this.implementationInstance.getClass();
        } else if (null != this.implementationFactory) {
            return TypeUtils.getGenericClass(this.implementationFactory.getClass(), 1);
        }
        return null;
    }

    public static ServiceDescriptor scoped(Type serviceType, Type implementationType) {
        return describe(serviceType, implementationType, ServiceLifetime.Scoped);
    }

    public static ServiceDescriptor scoped(Type serviceType, Func<Object, ServiceProvider> factory) {
        return describe(serviceType, factory, ServiceLifetime.Scoped);
    }

    public static ServiceDescriptor singleton(Type serviceType, Type implementationType) {
        return describe(serviceType, implementationType, ServiceLifetime.Singleton);
    }

    public static ServiceDescriptor singleton(Type serviceType, Func<Object, ServiceProvider> factory) {
        return describe(serviceType, factory, ServiceLifetime.Singleton);
    }

    public static ServiceDescriptor describe(Type serviceType, Type implementationType, ServiceLifetime lifetime) {
        return new ServiceDescriptor(serviceType, implementationType, lifetime);
    }


    public static ServiceDescriptor describe(Type serviceType, Func<Object, ServiceProvider> implementationFactory, ServiceLifetime lifetime) {
        return new ServiceDescriptor(serviceType, implementationFactory, lifetime);
    }
}
