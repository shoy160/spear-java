package cn.spear.protocol.tcp.sender;

import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.service.ServiceAddress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * @author shay
 * @date 2020/9/11
 */
public class TcpClientSender extends BaseSender implements MessageSender, AutoCloseable {
    private final Channel channel;
    private final ServiceAddress address;

    public TcpClientSender(MessageCodec messageCodec, Channel channel, ServiceAddress address) {
        super(messageCodec);
        this.channel = channel;
        this.address = address;
    }

    @Override
    public void send(BaseMessage message, boolean flush) {
        ByteBuf buffer = getBuffer(message, this.address.getGzip());
        if (flush) {
            this.channel.writeAndFlush(buffer);
        } else {
            this.channel.write(buffer);
        }
    }

    @Override
    public void close() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.disconnect();
        }
    }
}
