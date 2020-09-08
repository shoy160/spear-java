package cn.spear.core.service;

import java.util.List;

/**
 * 服务注册
 *
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceRegister {
    /**
     * 服务注册
     *
     * @param services 服务列表
     * @param address  服务地址
     */
    void regist(List<Class<?>> services, ServiceAddress address);

    /**
     * 注销服务注册
     */
    void deregist();
}
