package cn.spear.core.service;

import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.message.model.impl.DefaultResultMessage;

/**
 * 服务客户端
 *
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceClient extends AutoCloseable {

    /**
     * 发送消息
     *
     * @param message 消息
     * @param timeout 超时时间(秒)
     * @return result
     */
    DefaultResultMessage send(DefaultInvokeMessage message, long timeout);

    /**
     * 发送消息
     *
     * @param message 消息
     * @return result
     */
    default DefaultResultMessage send(DefaultInvokeMessage message) {
        return send(message, -1);
    }
}
