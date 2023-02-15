package cn.spear.core.message.event;

import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.BaseMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author shay
 * @date 2020/9/11
 */
public class MessageEventSource {
    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>();

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
        listeners.add(listener);
    }

    public void removeListener(MessageListener listener) {
        if (!listeners.isEmpty()) {
            listeners.remove(listener);
        }
    }

    public void onReceive(MessageSender sender, BaseMessage message) {
        MessageEvent event = new MessageEvent(sender, message);
        for (MessageListener listener : listeners) {
            listener.onReceived(event);
        }
    }
}
