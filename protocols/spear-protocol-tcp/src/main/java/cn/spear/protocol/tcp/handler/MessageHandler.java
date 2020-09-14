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

    public MessageHandler(MessageCodec codec, boolean gzip) {
        this.codec = codec;
        this.gzip = gzip;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf buffer) throws Exception {
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        Class<T> clazz = TypeUtils.getGenericClass(getClass(), 0);
        T serviceMsg = this.codec.decodeT(data, clazz, this.gzip);
        context.fireChannelRead(serviceMsg);
        ReferenceCountUtil.release(buffer);
    }
}
