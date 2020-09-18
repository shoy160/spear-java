package cn.spear.core.service;

import cn.spear.core.service.annotation.SpearService;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.TypeUtils;

/**
 * @author shay
 * @date 2020/9/18
 */
public interface ServiceRouter {

    /**
     * 获取服务名
     *
     * @param serviceClazz 服务类型
     * @return name
     */
    default String getServiceName(Class<?> serviceClazz) {
        return TypeUtils.getServiceName(serviceClazz);
    }
}
