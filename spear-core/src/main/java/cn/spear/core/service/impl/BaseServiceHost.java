package cn.spear.core.service.impl;

import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.message.model.InvokeMessage;
import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.InvokeMessageImpl;
import cn.spear.core.service.ServiceExecutor;
import cn.spear.core.service.ServiceHost;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;

/**
 * 服务主机抽象类
 *
 * @author shay
 * @date 2020/9/4
 */
@Slf4j
public abstract class BaseServiceHost implements ServiceHost {
    private final ServiceExecutor executor;
    private final Collection<MessageListener> listeners;

    protected BaseServiceHost(ServiceExecutor executor) {
        this.executor = executor;
        this.listeners = new HashSet<>();
        this.listeners.add(this::receivedMessage);
    }

    @Override
    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    private void receivedMessage(MessageSender sender, BaseMessage message) {
        if (!(message instanceof InvokeMessage)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("receive:{}", message.toString());
        }
        executor.execute(sender, (InvokeMessageImpl) message);
    }

    @Override
    public void received(MessageSender sender, BaseMessage message) {
        for (MessageListener listener : listeners) {
            listener.received(sender, message);
        }
    }
}
