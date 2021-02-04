package cn.spear.protocol.tcp.handler;

import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.model.impl.BaseMessage;
import cn.spear.core.util.TypeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @author shay
 * @date 2020/9/14
 */
public class MessageHandler<T extends BaseMessage> extends SimpleChannelInboundHandler<ByteBuf> {

    private final MessageCodec codec;
    private final boolean gzip;
    private final Class<T> clazz;

    public MessageHandler(MessageCodec codec, boolean gzip, Class<T> clazz) {
        this.codec = codec;
        this.gzip = gzip;
        this.clazz = clazz;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf buffer) throws Exception {
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        T serviceMsg = this.codec.decodeT(data, this.clazz, this.gzip);
        ReferenceCountUtil.release(buffer);
        context.fireChannelRead(serviceMsg);
    }
}
