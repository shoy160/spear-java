package cn.spear.core.service;

/**
 * 服务客户端工程
 *
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceClientFactory {
    /**
     * 创建客户端
     *
     * @param address 服务地址
     * @return 客户端
     */
    ServiceClient create(ServiceAddress address);

    /**
     * 关闭
     */
    void close();
}
