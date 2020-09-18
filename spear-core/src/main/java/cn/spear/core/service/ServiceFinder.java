package cn.spear.core.service;

import cn.spear.core.util.CommonUtils;

import java.util.List;

/**
 * 服务发现
 *
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceFinder extends ServiceRouter {
    /**
     * 服务发现
     *
     * @param serviceClazz 服务类型
     * @return list
     */
    default List<ServiceAddress> find(Class<?> serviceClazz) {
        String serviceName = getServiceName(serviceClazz);
        if (CommonUtils.isEmpty(serviceName)) {
            return null;
        }
        return find(serviceName);
    }

    /**
     * 服务发现
     *
     * @param serviceName 服务类型
     * @return list
     */
    List<ServiceAddress> find(String serviceName);

    /**
     * 清空服务缓存
     *
     * @param serviceClazz 服务类型
     */
    default void clean(Class<?> serviceClazz) {
        String serviceName = getServiceName(serviceClazz);
        if (CommonUtils.isEmpty(serviceName)) {
            return;
        }
        clean(serviceName);
    }

    /**
     * 清空服务缓存
     *
     * @param serviceName 服务名
     */
    void clean(String serviceName);
}
