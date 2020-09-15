package cn.spear.core.service.impl;

import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.model.InvokeMessage;
import cn.spear.core.message.model.ResultMessage;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.ServiceClient;
import cn.spear.core.service.ServiceExecutor;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CompletionStage;

/**
 * @author shay
 * @date 2020/9/14
 */
@Slf4j
public class DefaultServiceClient implements ServiceClient {
    private final MessageSender sender;
    private final MessageListener listener;
    private final Map<String, DefaultResultMessage> receiveMap;

    public DefaultServiceClient(MessageSender sender, MessageListener listener, ServiceExecutor executor) {
        this.sender = sender;
        this.listener = listener;
        receiveMap = new HashMap<>();
        this.listener.addListener(event -> {
            BaseMessage message = event.getMessage();
            log.info("client receive:{}", message.toString());
            if (message instanceof DefaultResultMessage) {
                receiveMap.put(message.getId(), (DefaultResultMessage) message);
            }
            if (null != executor && message instanceof DefaultInvokeMessage) {
                executor.execute(event.getSender(), (DefaultInvokeMessage) message);
            }
        });
    }

    @Override
    public DefaultResultMessage send(DefaultInvokeMessage message) {
        this.sender.send(message);
        if (message.getNotify()) {
            return new DefaultResultMessage();
        }
        while (true) {
            if (receiveMap.containsKey(message.getId())) {
                DefaultResultMessage result = receiveMap.get(message.getId());
                receiveMap.remove(message.getId());
                return result;
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
