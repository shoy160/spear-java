package cn.spear.core.message.event;

import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.BaseMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author shay
 * @date 2020/9/11
 */
public class MessageEventSource {
    private final Semaphore semaphore = new Semaphore(1);
    private final List<MessageListener> listeners = new ArrayList<>();

    private static volatile MessageEventSource instance;

    private MessageEventSource() {
    }

    public static synchronized MessageEventSource getInstance() {
        if (null == instance) {
            instance = new MessageEventSource();
        }
        return instance;
    }

    public void addListener(MessageListener listener) {
        try {
            semaphore.acquire(1);
            listeners.add(listener);
            semaphore.release(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void removeListener(MessageListener listener) {
        try {
            semaphore.acquire(1);
            if (!listeners.isEmpty()) {
                listeners.remove(listener);
            }
            semaphore.release(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onReceive(MessageSender sender, BaseMessage message) {
        try {
            semaphore.acquire(1);
            MessageEvent event = new MessageEvent(sender, message);
            for (MessageListener listener : listeners) {
                listener.onReceived(event);
            }
            semaphore.release(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
