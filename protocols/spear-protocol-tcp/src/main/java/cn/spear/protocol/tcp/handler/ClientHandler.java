package cn.spear.protocol.tcp.handler;

import cn.spear.core.lang.Action;
import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.ServiceAddress;
import cn.spear.protocol.tcp.TcpAttributes;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

/**
 * @author shay
 * @date 2020/9/14
 */
@RequiredArgsConstructor
public class ClientHandler extends SimpleChannelInboundHandler<DefaultResultMessage> {

    private final Action<ServiceAddress> removeAction;

    @Override
    protected void channelRead0(ChannelHandlerContext context, DefaultResultMessage message) {
        MessageListener listener = context.channel().attr(TcpAttributes.LISTENER_KEY).get();
        MessageSender sender = context.channel().attr(TcpAttributes.SENDER_KEY).get();

        listener.onReceived(new MessageEvent(sender, message));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ServiceAddress address = ctx.channel().attr(TcpAttributes.ADDRESS_KEY).get();
        if(null != removeAction) {
            removeAction.invoke(address);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ServiceAddress address = ctx.channel().attr(TcpAttributes.ADDRESS_KEY).get();
        if (null != removeAction) {
            removeAction.invoke(address);
        }
    }
}
