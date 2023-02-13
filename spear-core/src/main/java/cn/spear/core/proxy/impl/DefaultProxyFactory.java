package cn.spear.core.proxy.impl;

import cn.spear.core.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shay
 * @date 2020/9/15
 */
public class DefaultProxyFactory implements ProxyFactory {
    private final ConcurrentMap<Class<?>, Map<InvocationHandler, Object>> proxyCache;

    public DefaultProxyFactory() {
        this.proxyCache = new ConcurrentHashMap<>();
    }

    @Override
    public Object create(Class<?> clazz, InvocationHandler handler) {
        Map<InvocationHandler, Object> instanceMap = proxyCache.getOrDefault(clazz, null);
        if (null == instanceMap) {
            instanceMap = new HashMap<>();
        }
        if (instanceMap.containsKey(handler)) {
            return instanceMap.get(handler);
        }
        ClassLoader loader = clazz.getClassLoader();
        List<Class<?>> interfaces = new ArrayList<>();
        if (clazz.isInterface()) {
            interfaces.add(clazz);
        }
        interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
        Object instance = Proxy.newProxyInstance(loader, interfaces.toArray(new Class[0]), handler);
        instanceMap.put(handler, instance);
        if (proxyCache.containsKey(clazz)) {
            proxyCache.replace(clazz, instanceMap);
        } else {
            proxyCache.put(clazz, instanceMap);
        }
        return instance;
    }
}
