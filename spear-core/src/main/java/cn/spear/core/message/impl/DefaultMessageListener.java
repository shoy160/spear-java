package cn.spear.core.message.impl;

import cn.spear.core.message.MessageListener;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.event.MessageEventSource;

/**
 * @author shay
 * @date 2020/9/11
 */
public class DefaultMessageListener implements MessageListener {
    @Override
    public void onReceived(MessageEvent event) {
        MessageEventSource eventSource = getEventSource();
        if (null == eventSource) {
            return;
        }
        eventSource.onReceive(event.getSender(), event.getMessage());
    }
}
