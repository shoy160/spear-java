package cn.spear.core.service.impl;

import cn.spear.core.ioc.IocContext;
import cn.spear.core.ioc.ServiceCollection;
import cn.spear.core.service.ServiceEntry;
import cn.spear.core.service.ServiceEntryFactory;
import cn.spear.core.service.ServiceGenerator;
import cn.spear.core.service.annotation.SpearService;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shay
 * @date 2020/9/8
 */
@Slf4j
public class DefaultServiceEntryFactory implements ServiceEntryFactory {
    private final ConcurrentMap<String, ServiceEntry> entryMap;
    private final List<Class<?>> serviceCache;
    private final ServiceGenerator serviceGenerator;
    private final ServiceCollection services;
    private final String basePackage;

    public DefaultServiceEntryFactory(ServiceCollection serviceCollection) {
        this(serviceCollection, "");
    }

    public DefaultServiceEntryFactory(ServiceCollection serviceCollection, String basePackage) {
        this.basePackage = basePackage;
        this.entryMap = new ConcurrentHashMap<>();
        this.serviceCache = new ArrayList<>();
        this.serviceGenerator = IocContext.getServiceT(ServiceGenerator.class);
        this.services = serviceCollection;
        findServices();

    }

    public Class<?> getImplClass(Class<?> clazz) {
        Set<Class<?>> classSet = ReflectUtils.findClasses(basePackage, c -> ReflectUtils.isImplClass(clazz, c));
        return CommonUtils.isEmpty(classSet) ? null : classSet.iterator().next();
    }

    private void findServices() {
        long time = System.currentTimeMillis();
        try {
            Set<Class<?>> classes = ReflectUtils.findClasses(basePackage, clazz -> clazz.getAnnotation(SpearService.class) != null);
            for (Class<?> clazz : classes) {
                Class<?> implClazz = getImplClass(clazz);
                //判断是否有实现类
                if (null == implClazz) {
                    continue;
                }
                this.services.addTransient(clazz, implClazz);
                serviceCache.add(clazz);
                Method[] methods = clazz.getMethods();
                // 过滤
                for (Method method : methods) {
                    String serviceKey = this.serviceGenerator.getServiceId(method);
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
    public ServiceEntry find(String serviceId) {
        if (entryMap.containsKey(serviceId)) {
            return entryMap.get(serviceId);
        }
        log.warn("未找到服务条目：{}", serviceId);
        return null;
    }
}
