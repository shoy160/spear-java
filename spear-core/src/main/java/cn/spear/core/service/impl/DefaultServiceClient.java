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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CompletionStage;

/**
 * @author shay
 * @date 2020/9/14
 */
@Slf4j
public class DefaultServiceClient implements ServiceClient, AutoCloseable {
    private final MessageSender sender;
    private final MessageListener listener;
    private final ServiceExecutor executor;

    public DefaultServiceClient(MessageSender sender, MessageListener listener, ServiceExecutor executor) {
        this.sender = sender;
        this.listener = listener;
        this.executor = executor;
        this.listener.addListener(new MessageListener() {
            @Override
            public void onReceived(MessageEvent event) {
                BaseMessage message = event.getMessage();
                log.info("client receive:{}", message.toString());
                if (null != executor && message instanceof DefaultInvokeMessage) {
                    executor.execute(event.getSender(), (DefaultInvokeMessage) message);
                }
            }
        });
    }

    @Override
    public DefaultResultMessage send(DefaultInvokeMessage message) {
        this.sender.send(message);
        return null;
    }

    @Override
    public void close() {
        try {
            if (sender instanceof AutoCloseable) {
                ((AutoCloseable) sender).close();
            }
            if (listener instanceof AutoCloseable) {
                ((AutoCloseable) listener).close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
