package cn.spear.core.service.impl;

import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.ServiceEntry;
import cn.spear.core.service.ServiceEntryFactory;
import cn.spear.core.service.ServiceExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author shay
 * @date 2020/9/8
 */
@Slf4j
public class DefaultServiceExecutor implements ServiceExecutor {

    private final ServiceEntryFactory entryFactory;
    private final ExecutorService fixedThreadPool;

    public DefaultServiceExecutor(ServiceEntryFactory entryFactory) {
        this.entryFactory = entryFactory;
        fixedThreadPool = Executors.newCachedThreadPool();
    }

    private void localExecute(ServiceEntry entry, DefaultInvokeMessage invokeMessage, DefaultResultMessage resultMessage) {
        try {
            if (entry.isNotify()) {
                entry.getInvoke().invoke(invokeMessage.getParameters());
            } else {
                Object result = entry.getInvoke().invoke(invokeMessage.getParameters());
                resultMessage.setContent(result);
            }
        } catch (Exception ex) {
            log.error("执行本地逻辑时候发生了错误。", ex);
            resultMessage.setCode(500);
            resultMessage.setMessage(ex.getMessage());
        }
    }

    private void send(MessageSender sender, String messageId, BaseMessage message) {
        try {
            message.setId(messageId);
            sender.send(message);
        } catch (Exception ex) {
            log.error("发送响应消息异常", ex);
        }
    }

    @Override
    public void execute(MessageSender sender, DefaultInvokeMessage message) {
        ServiceEntry entry = this.entryFactory.find(message.getServiceId());
        if (null == entry) {
            send(sender, message.getId(), new DefaultResultMessage("服务未找到", 404));
            return;
        }
        DefaultResultMessage result = new DefaultResultMessage();
        if (entry.isNotify()) {
            send(sender, message.getId(), result);
            this.fixedThreadPool.execute(() -> localExecute(entry, message, result));
        } else {
            localExecute(entry, message, result);
            send(sender, message.getId(), result);
        }

    }
}
