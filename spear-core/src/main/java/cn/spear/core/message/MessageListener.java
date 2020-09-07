package cn.spear.core.message;

import cn.spear.core.message.model.impl.BaseMessage;

/**
 * 消息接收器
 *
 * @author shay
 * @date 2020/9/4
 */
public interface MessageListener {
    /**
     * 接收到消息
     *
     * @param sender  消息发送器
     * @param message 消息体
     */
    void received(MessageSender sender, BaseMessage message);
}
