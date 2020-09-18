package cn.spear.core.service;

import cn.spear.core.service.annotation.SpearService;
import cn.spear.core.util.CommonUtils;

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
        SpearService service = serviceClazz.getAnnotation(SpearService.class);
        if (null == service || CommonUtils.isEmpty(service.service())) {
            return serviceClazz.getName();
        }
        return service.service();
    }
}
