package cn.spear.core.service;

import cn.spear.core.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务注册
 *
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceRegister extends ServiceRouter {
    /**
     * 服务注册
     *
     * @param services 服务列表
     * @param address  服务地址
     */
    default void regist(List<Class<?>> services, ServiceAddress address) {
        if (CommonUtils.isEmpty(services)) {
            return;
        }
        List<String> serviceIds = new ArrayList<>();
        for (Class<?> service : services) {
            String serviceId = getServiceName(service);
            if (CommonUtils.isNotEmpty(serviceId) && !serviceIds.contains(serviceId)) {
                regist(serviceId, address);
                serviceIds.add(serviceId);
            }
        }
    }

    /**
     * 服务注册
     *
     * @param serviceName 服务名
     * @param address     服务地址
     */
    void regist(String serviceName, ServiceAddress address);

    /**
     * 注销服务注册
     */
    void deregist();
}
