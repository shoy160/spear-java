package cn.spear.core.service;

import java.util.List;

/**
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceFinder {
    /**
     * 服务发现
     *
     * @param serviceClazz 服务类型
     * @return list
     */
    List<ServiceAddress> find(Class<?> serviceClazz);

    /**
     * 清空服务缓存
     *
     * @param serviceClazz 服务类型
     */
    void clean(Class<?> serviceClazz);
}
