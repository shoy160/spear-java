package cn.spear.protocol.tcp.sender;

import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.service.ServiceAddress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shay
 * @date 2020/9/11
 */
public class TcpServerSender extends BaseSender implements MessageSender {
    private final ChannelHandlerContext context;
    private final ServiceAddress address;

    public TcpServerSender(MessageCodec messageCodec, ChannelHandlerContext context, ServiceAddress address) {
        super(messageCodec);
        this.context = context;
        this.address = address;
    }

    @Override
    public void send(BaseMessage message, boolean flush) {
        ByteBuf buffer = getBuffer(message, this.address.getGzip());
        if (flush) {
            this.context.writeAndFlush(buffer);
        } else {
            this.context.write(buffer);
        }
    }
}
