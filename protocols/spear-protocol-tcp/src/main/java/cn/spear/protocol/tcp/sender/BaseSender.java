package cn.spear.protocol.tcp.sender;

import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.model.impl.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 消息发送
 *
 * @author shay
 * @date 2020/9/11
 */
public abstract class BaseSender {
    private final MessageCodec messageCodec;

    protected BaseSender(MessageCodec messageCodec) {
        this.messageCodec = messageCodec;
    }

    protected ByteBuf getBuffer(BaseMessage message, boolean gzip) {
        byte[] data = this.messageCodec.encode(message, gzip);
        return Unpooled.wrappedBuffer(data);
    }
}
