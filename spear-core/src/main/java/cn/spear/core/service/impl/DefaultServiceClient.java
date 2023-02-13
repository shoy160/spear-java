package cn.spear.core.service.impl;

import cn.spear.core.exception.BusiException;
import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.ServiceClient;
import cn.spear.core.service.ServiceExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author shay
 * @date 2020/9/14
 */
@Slf4j
public class DefaultServiceClient implements ServiceClient {
    private final MessageSender sender;
    private final MessageListener listener;
    private final ServiceExecutor executor;
    private final Map<String, CompletableFuture<DefaultResultMessage>> receiveMap;

    public DefaultServiceClient(MessageSender sender, MessageListener listener, ServiceExecutor executor) {
        this.sender = sender;
        this.listener = listener;
        this.receiveMap = new HashMap<>();
        this.executor = executor;
        this.listener.addListener(this::onReceived);
    }

    private void onReceived(MessageEvent event) {
        BaseMessage message = event.getMessage();
        if (log.isDebugEnabled()) {
            log.debug("client receive result:{}", message.getId());
        }
        if (message instanceof DefaultResultMessage) {
            CompletableFuture<DefaultResultMessage> futureTask = this.receiveMap.get(message.getId());
            if (null != futureTask) {
                futureTask.complete((DefaultResultMessage) message);
            }
        } else if (null != this.executor && message instanceof DefaultInvokeMessage) {
            this.executor.execute(event.getSender(), (DefaultInvokeMessage) message);
        }
    }

    @Override
    public DefaultResultMessage send(DefaultInvokeMessage message, long timeout) {
        if (message.getNotify()) {
            this.sender.send(message);
            return new DefaultResultMessage();
        } else {
            CompletableFuture<DefaultResultMessage> futureTask = new CompletableFuture<>();
            receiveMap.put(message.getId(), futureTask);
            this.sender.send(message);
            try {
                if (timeout > 0) {
                    return futureTask.get(timeout, TimeUnit.SECONDS);
                } else {
                    return futureTask.get();
                }
            } catch (TimeoutException e) {
                return new DefaultResultMessage("RPC请求超时", 504);
            } catch (Exception e) {
                if (e instanceof BusiException) {
                    return new DefaultResultMessage(e.getMessage(), ((BusiException) e).getCode());
                }
                Logger logger = LoggerFactory.getLogger(getClass());
                logger.error("RPC请求异常", e);
                return new DefaultResultMessage("RPC请求异常", 500);
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
        log.debug("client closed");
    }
}
