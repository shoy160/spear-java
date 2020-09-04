package cn.spear.core.service;

import cn.spear.core.message.InvokeMessage;
import cn.spear.core.message.ResultMessage;

/**
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
    ResultMessage<?> send(InvokeMessage<?> message);

    /**
     * 关闭客户端
     */
    void close();
}
