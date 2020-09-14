package cn.spear.core.ioc.impl;

import cn.spear.core.ioc.ServiceDescriptor;
import cn.spear.core.ioc.ServiceLifetime;
import cn.spear.core.ioc.ServiceProvider;
import com.sun.corba.se.spi.ior.ObjectKey;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shay
 * @date 2020/9/8
 */
public class ServiceProviderImpl implements ServiceProvider {
    private final List<ServiceDescriptor> descriptors;
    private final ConcurrentMap<Type, Object> serviceCache;

    public ServiceProviderImpl(List<ServiceDescriptor> descriptors) {
        this.descriptors = descriptors;
        serviceCache = new ConcurrentHashMap<>();
    }

    @Override
    public Object getService(Type serviceType) {
        if (serviceCache.containsKey(serviceType)) {
            return serviceCache.get(serviceType);
        }
        for (ServiceDescriptor descriptor : descriptors) {
            if (serviceType.equals(descriptor.getServiceType())) {
                Object service = null;
                if (descriptor.getImplementationInstance() != null) {
                    service = descriptor.getImplementationInstance();
                } else if (null != descriptor.getImplementationFactory()) {
                    service = descriptor.getImplementationFactory().invoke(this);
                } else if (null != descriptor.getImplementationType()) {
                    try {
                        service = ((Class<?>) descriptor.getImplementationType()).newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (service == null) {
                    continue;
                }
                if (descriptor.getLifetime() != ServiceLifetime.Transient) {
                    serviceCache.putIfAbsent(serviceType, service);
                }
                return service;
            }
        }
        return null;
    }
}
