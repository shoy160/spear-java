package cn.spear.core.service.impl;

import cn.spear.core.ioc.IocContext;
import cn.spear.core.service.ServiceEntry;
import cn.spear.core.service.ServiceEntryFactory;
import cn.spear.core.service.annotation.SpearService;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shay
 * @date 2020/9/8
 */
@Slf4j
public class DefaultServiceEntryFactory implements ServiceEntryFactory {
    private final Map<Method, String> routeCache;
    private final ConcurrentMap<String, ServiceEntry> entryMap;
    private final List<Class<?>> serviceCache;

    public DefaultServiceEntryFactory() {
        entryMap = new ConcurrentHashMap<>();
        routeCache = new HashMap<>();
        serviceCache = new ArrayList<>();
        findServices();
    }

    public Class<?> getImplClass(Class<?> clazz) {
        Set<Class<?>> classSet = TypeUtils.findClasses(c -> clazz.isAssignableFrom(c) && !c.isInterface() && !Modifier.isAbstract(c.getModifiers()));
        return CommonUtils.isEmpty(classSet) ? null : classSet.iterator().next();
    }

    public Object getInstance(Class<?> interfaceClazz) {
        Class<?> implClazz = getImplClass(interfaceClazz);
        if (null == implClazz) {
            return null;
        }
        try {
            return implClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void findServices() {
        long time = System.currentTimeMillis();
        try {
            Set<Class<?>> classes = TypeUtils.findClasses(clazz -> clazz.getAnnotation(SpearService.class) != null);
            for (Class<?> clazz : classes) {
                //判断是否有实现类
                if (null == getImplClass(clazz)) {
                    continue;
                }
                serviceCache.add(clazz);
                Method[] methods = clazz.getMethods();
                // 过滤
                for (Method method : methods) {
                    String serviceKey = getServiceId(method);
                    ServiceEntry entry = new ServiceEntry(method);
                    entry.setInvoke(params -> {
                        Object instance = IocContext.getService(clazz);
                        if (null == instance) {
                            return null;
                        }
                        List<Object> args = new ArrayList<>();
                        for (Map.Entry<String, Object> item : params.entrySet()) {
                            args.add(item.getValue());
                        }
                        return method.invoke(instance, args.toArray());
                    });
                    entryMap.putIfAbsent(serviceKey, entry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("服务查找 -> {}项服务，{}条服务条目，耗时：{}ms", serviceCache.size(), entryMap.size(), (System.currentTimeMillis() - time));
        }
    }

    @Override
    public Map<String, ServiceEntry> getEntries() {
        return entryMap;
    }

    @Override
    public List<Class<?>> getServices() {
        return this.serviceCache;
    }

    @Override
    public String getServiceId(Method method) {
        if (routeCache.containsKey(method)) {
            return routeCache.get(method);
        }
        Class<?> clazz = method.getDeclaringClass();
        SpearService clazzService = clazz.getAnnotation(SpearService.class);
        String prefix = null == clazzService ? clazz.getName() : clazzService.route();
        SpearService service = method.getAnnotation(SpearService.class);
        String methodRoute;
        if (null != service && CommonUtils.isNotEmpty(service.route())) {
            methodRoute = service.route();
        } else {
            StringBuilder nameBuilder = new StringBuilder(method.getName());
            Parameter[] parameters = method.getParameters();
            if (CommonUtils.isNotEmpty(parameters)) {
                for (Parameter parameter : parameters) {
                    nameBuilder.append("_").append(parameter.getType().getSimpleName());
                }
            }
            methodRoute = nameBuilder.toString().toLowerCase();
        }
        String route = String.format("%s/%s", prefix, methodRoute);
        if (log.isInfoEnabled()) {
            log.info("{}_{},生成服务条目ID:{}", method.getDeclaringClass().getName(), method.getName(), route);
        }

        routeCache.put(method, route);
        return route;
    }

    @Override
    public ServiceEntry find(String serviceId) {
        if (entryMap.containsKey(serviceId)) {
            return entryMap.get(serviceId);
        }
        log.warn("未找到服务条目：{}", serviceId);
        return null;
    }
}
