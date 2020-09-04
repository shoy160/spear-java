package cn.spear.core.service;

import cn.spear.core.message.InvokeMessage;
import cn.spear.core.message.MessageSender;

/**
 * @author shay
 * @date 2020/9/4
 */
public interface ServiceExecutor {
    /**
     * 执行消息发送
     *
     * @param sender  发送者
     * @param message 调用消息
     */
    void execute(MessageSender sender, InvokeMessage<?> message);
}
