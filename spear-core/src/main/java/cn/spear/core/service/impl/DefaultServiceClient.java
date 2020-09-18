package cn.spear.core.service.impl;

import cn.spear.core.exception.BusiException;
import cn.spear.core.message.MessageFutureTask;
import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.ServiceClient;
import cn.spear.core.service.ServiceExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author shay
 * @date 2020/9/14
 */
@Slf4j
public class DefaultServiceClient implements ServiceClient {
    private final MessageSender sender;
    private final MessageListener listener;
    private final Map<String, MessageFutureTask<DefaultResultMessage>> receiveMap;

    public DefaultServiceClient(MessageSender sender, MessageListener listener, ServiceExecutor executor) {
        this.sender = sender;
        this.listener = listener;
        receiveMap = new HashMap<>();
        this.listener.addListener(event -> {
            BaseMessage message = event.getMessage();
//            log.info("client receive result:{}", message.getId());
            if (message instanceof DefaultResultMessage) {
                MessageFutureTask<DefaultResultMessage> futureTask = receiveMap.get(message.getId());
                if (null != futureTask) {
                    futureTask.setResult((DefaultResultMessage) message);
                }
            }
            if (null != executor && message instanceof DefaultInvokeMessage) {
                executor.execute(event.getSender(), (DefaultInvokeMessage) message);
            }
        });
    }

    @Override
    public DefaultResultMessage send(DefaultInvokeMessage message, long timeout) {
        this.sender.send(message);
        if (message.getNotify()) {
            return new DefaultResultMessage();
        } else {
            MessageFutureTask<DefaultResultMessage> futureTask = new MessageFutureTask<>(message);
            receiveMap.put(message.getId(), futureTask);
            try {
                timeout = timeout <= 0 ? 30 : timeout;
                return futureTask.get(timeout, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException | BusiException e) {
                e.printStackTrace();
                return new DefaultResultMessage("RPC请求超时", 504);
            } finally {
                receiveMap.remove(message.getId());
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (sender instanceof AutoCloseable) {
            ((AutoCloseable) sender).close();
        }
        if (listener instanceof AutoCloseable) {
            ((AutoCloseable) listener).close();
        }
        log.info("client closed");
    }
}
