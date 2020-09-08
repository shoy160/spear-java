package cn.spear.core.service.impl;

import cn.spear.core.service.ServiceEntry;
import cn.spear.core.service.ServiceEntryFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author shay
 * @date 2020/9/8
 */
public class ServiceEntryFactoryImpl implements ServiceEntryFactory {
    @Override
    public Map<String, ServiceEntry> getEntries() {
        return null;
    }

    @Override
    public List<Class<?>> getServices() {
        return null;
    }

    @Override
    public String getServiceId(Method method) {
        return null;
    }

    @Override
    public ServiceEntry find(String serviceId) {
        return null;
    }
}
