package cn.spear.core.service.impl;

import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceFinder;
import cn.spear.core.service.ServiceRegister;
import cn.spear.core.util.CommonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shay
 * @date 2020/9/8
 */
public class DefaultServiceRouter implements ServiceRegister, ServiceFinder {

    private final ConcurrentMap<Type, List<ServiceAddress>> serviceCache;

    public DefaultServiceRouter() {
        this.serviceCache = new ConcurrentHashMap<>();
    }

    @Override
    public List<ServiceAddress> find(Class<?> serviceClazz) {
        if (serviceCache.containsKey(serviceClazz)) {
            return serviceCache.get(serviceClazz);
        }
        return null;
    }

    @Override
    public void clean(Class<?> serviceClazz) {
        serviceCache.remove(serviceClazz);
    }

    @Override
    public void regist(List<Class<?>> services, ServiceAddress address) {
        if (CommonUtils.isEmpty(services)) {
            return;
        }
        for (Class<?> service : services) {
            List<ServiceAddress> addressList;
            if (serviceCache.containsKey(service)) {
                addressList = serviceCache.get(service);
                addressList.add(address);
                serviceCache.replace(service, addressList);
            } else {
                addressList = new ArrayList<>();
                addressList.add(address);
                serviceCache.putIfAbsent(service, addressList);
            }
        }
    }

    @Override
    public void deregist() {
        serviceCache.clear();
    }
}