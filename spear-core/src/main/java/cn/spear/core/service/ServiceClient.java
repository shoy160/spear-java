package cn.spear.core.service;

import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.message.model.impl.DefaultResultMessage;

/**
 * 服务客户端
 *
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceClient {

    /**
     * 发送消息
     *
     * @param message 消息
     * @return result
     */
    DefaultResultMessage send(DefaultInvokeMessage message);

    /**
     * 关闭客户端
     */
    void close();
}
