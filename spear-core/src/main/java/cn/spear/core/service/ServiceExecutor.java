package cn.spear.core.service;

import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.InvokeMessageImpl;

/**
 * 服务执行器
 *
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
    void execute(MessageSender sender, InvokeMessageImpl message);
}
