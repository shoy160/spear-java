package cn.spear.core.message;

import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.event.MessageEventSource;

import java.util.EventListener;

/**
 * 消息接收器
 *
 * @author shay
 * @date 2020/9/4
 */
public interface MessageListener extends EventListener {

    /**
     * 获取事件资源
     *
     * @return MessageEventSource
     */
    default MessageEventSource getEventSource() {
        return MessageEventSource.getInstance();
    }

    /**
     * 添加监听
     *
     * @param listener listener
     */
    default void addListener(MessageListener listener) {
        MessageEventSource eventSource = getEventSource();
        if (null == eventSource) {
            return;
        }
        eventSource.addListener(listener);
    }

    /**
     * 接收到消息
     *
     * @param event 事件
     */
    void onReceived(MessageEvent event);
}
