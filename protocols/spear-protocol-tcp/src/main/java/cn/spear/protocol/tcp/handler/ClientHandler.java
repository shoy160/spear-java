package cn.spear.protocol.tcp.handler;

import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.ServiceAddress;
import cn.spear.protocol.tcp.ChannelAttributes;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.Consumer;

/**
 * @author shay
 * @date 2020/9/14
 */
public class ClientHandler extends SimpleChannelInboundHandler<DefaultResultMessage> {
    private final Consumer<ServiceAddress> removeConsumer;

    public ClientHandler(Consumer<ServiceAddress> removeConsumer) {
        this.removeConsumer = removeConsumer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, DefaultResultMessage message) throws Exception {
        MessageListener listener = context.channel().attr(ChannelAttributes.LISTENER_KEY).get();
        MessageSender sender = context.channel().attr(ChannelAttributes.SENDER_KEY).get();
        listener.onReceived(new MessageEvent(sender, message));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ServiceAddress address = ctx.channel().attr(ChannelAttributes.ADDRESS_KEY).get();
        removeConsumer.accept(address);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
