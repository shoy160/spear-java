package cn.spear.core.service.impl;

import cn.spear.core.message.BaseMessage;
import cn.spear.core.message.InvokeMessage;
import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.service.ServiceExecutor;
import cn.spear.core.service.ServiceHost;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author shay
 * @date 2020/9/4
 */
public abstract class AbstractServiceHost implements ServiceHost {
    private final ServiceExecutor executor;
    private final Collection<MessageListener> listeners;

    protected AbstractServiceHost(ServiceExecutor executor) {
        this.executor = executor;
        this.listeners = new HashSet<>();
        this.listeners.add((sender, message) -> {
            if (!(message instanceof InvokeMessage)) {
                return;
            }
            executor.execute(sender, (InvokeMessage<?>) message);
        });
    }



    @Override
    public void received(MessageSender sender, BaseMessage message) {
        for (MessageListener listener : listeners) {
            listener.received(sender, message);
        }
    }
}
