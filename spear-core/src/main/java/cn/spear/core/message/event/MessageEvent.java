package cn.spear.core.message.event;

import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.BaseMessage;

import java.util.EventObject;

/**
 * @author shay
 * @date 2020/9/11
 */
public class MessageEvent extends EventObject {
    private final MessageSender sender;
    private final BaseMessage message;

    public MessageSender getSender() {
        return this.sender;
    }

    public BaseMessage getMessage() {
        return this.message;
    }

    public MessageEvent(MessageSender sender, BaseMessage message) {
        super(MessageEventSource.getInstance());
        this.sender = sender;
        this.message = message;
    }
}
