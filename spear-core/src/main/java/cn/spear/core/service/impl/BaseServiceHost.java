package cn.spear.core.service.impl;

import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.InvokeMessage;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.service.ServiceExecutor;
import cn.spear.core.service.ServiceHost;
import cn.spear.core.service.ServiceListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务主机抽象类
 *
 * @author shay
 * @date 2020/9/4
 */
@Slf4j
public abstract class BaseServiceHost implements ServiceHost {
    private final ServiceExecutor executor;
    private final ServiceListener listener;

    protected BaseServiceHost(ServiceExecutor executor, ServiceListener listener) {
        this.executor = executor;
        this.listener = listener;
        this.listener.addListener(event -> receivedMessage(event.getSender(), event.getMessage()));
    }

    protected ServiceListener getListener() {
        return this.listener;
    }

    private void receivedMessage(MessageSender sender, BaseMessage message) {
        if (!(message instanceof InvokeMessage)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("receive:{}", message.toString());
        }
        executor.execute(sender, (DefaultInvokeMessage) message);
    }
}
