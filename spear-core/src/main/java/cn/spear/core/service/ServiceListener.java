package cn.spear.core.service;

import cn.spear.core.message.MessageListener;

/**
 * @author shay
 * @date 2020/9/8
 */
public interface ServiceListener extends MessageListener {
    /**
     * 启动服务
     *
     * @param address 服务地址
     */
    void start(ServiceAddress address);

    /**
     * 启动服务
     *
     * @param host host
     * @param port port
     */
    default void start(String host, int port) {
        start(new ServiceAddress(host, port));
    }

    /**
     * 添加监听
     *
     * @param listener listener
     */
    void addListener(MessageListener listener);

    /**
     * 停止服务
     */
    void stop();
}