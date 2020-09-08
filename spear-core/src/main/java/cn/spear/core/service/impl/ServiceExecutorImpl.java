package cn.spear.core.service.impl;

import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.message.model.impl.InvokeMessageImpl;
import cn.spear.core.message.model.impl.ResultMessageImpl;
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
public class ServiceExecutorImpl implements ServiceExecutor {

    private final ServiceEntryFactory entryFactory;
    private final ExecutorService fixedThreadPool;

    public ServiceExecutorImpl(ServiceEntryFactory entryFactory) {
        this.entryFactory = entryFactory;
        fixedThreadPool = Executors.newCachedThreadPool();
    }

    private void localExecute(ServiceEntry entry, InvokeMessageImpl invokeMessage, ResultMessageImpl resultMessage) {
        try {
            if (entry.isNotify()) {
                entry.getInvoke().apply(invokeMessage.getParameters());
            } else {
                Object result = entry.getInvoke().apply(invokeMessage.getParameters());
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
    public void execute(MessageSender sender, InvokeMessageImpl message) {
        ServiceEntry entry = this.entryFactory.find(message.getServiceId());
        if (null == entry) {
            send(sender, message.getId(), new ResultMessageImpl("服务未找到", 404));
            return;
        }
        ResultMessageImpl result = new ResultMessageImpl();
        if (entry.isNotify()) {
            send(sender, message.getId(), result);
            this.fixedThreadPool.execute(() -> localExecute(entry, message, result));
        } else {
            localExecute(entry, message, result);
            send(sender, message.getId(), result);
        }

    }
}
