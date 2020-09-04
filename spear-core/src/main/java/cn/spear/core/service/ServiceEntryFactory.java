package cn.spear.core.service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 服务条目发现工厂
 *
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceEntryFactory {
    /**
     * 获取服务条目
     *
     * @return map
     */
    Map<String, ServiceEntry> getEntries();

    /**
     * 获取服务列表
     *
     * @return list
     */
    List<Class<?>> getServices();

    /**
     * 获取服务条目Id
     *
     * @param method method
     * @return serviceId
     */
    String getServiceId(Method method);

    /**
     * 查找服务条目
     *
     * @param serviceId 服务条目ID
     * @return 服务条目
     */
    ServiceEntry find(String serviceId);
}
