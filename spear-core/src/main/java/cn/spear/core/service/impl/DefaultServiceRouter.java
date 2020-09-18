package cn.spear.core.service.impl;

import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceFinder;
import cn.spear.core.service.ServiceRegister;
import cn.spear.core.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shay
 * @date 2020/9/8
 */
public class DefaultServiceRouter implements ServiceRegister, ServiceFinder {

    private final ConcurrentMap<String, List<ServiceAddress>> serviceCache;

    public DefaultServiceRouter() {
        this.serviceCache = new ConcurrentHashMap<>();
    }

    public DefaultServiceRouter(Map<String, List<ServiceAddress>> services) {
        this.serviceCache = new ConcurrentHashMap<>(services);
    }

    @Override
    public List<ServiceAddress> find(String serviceName) {
        if (null != serviceName && serviceCache.containsKey(serviceName)) {
            return serviceCache.get(serviceName);
        }
        return null;
    }

    @Override
    public void clean(String serviceName) {
        if (CommonUtils.isNotEmpty(serviceName)) {
            serviceCache.remove(serviceName);
        }
    }

    @Override
    public void regist(String serviceName, ServiceAddress address) {
        if (CommonUtils.isEmpty(serviceName)) {
            return;
        }
        List<ServiceAddress> addressList;
        if (serviceCache.containsKey(serviceName)) {
            addressList = serviceCache.get(serviceName);
            addressList.add(address);
            serviceCache.replace(serviceName, addressList);
        } else {
            addressList = new ArrayList<>();
            addressList.add(address);
            serviceCache.putIfAbsent(serviceName, addressList);
        }
    }

    @Override
    public void deregist() {
        serviceCache.clear();
    }
}
