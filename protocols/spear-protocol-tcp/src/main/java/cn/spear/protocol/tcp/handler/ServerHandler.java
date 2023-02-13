package cn.spear.protocol.tcp.handler;

import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.MessageListener;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.service.ServiceAddress;
import cn.spear.protocol.tcp.sender.TcpServerSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shay
 * @date 2020/9/14
 */
public class ServerHandler extends SimpleChannelInboundHandler<DefaultInvokeMessage> {
    private final MessageCodec codec;
    private final ServiceAddress address;
    private final MessageListener messageListener;

    public ServerHandler(MessageCodec codec, ServiceAddress address, MessageListener messageListener) {
        this.codec = codec;
        this.address = address;
        this.messageListener = messageListener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, DefaultInvokeMessage invokeMessage) {
        TcpServerSender sender = new TcpServerSender(codec, context, address);
        messageListener.onReceived(new MessageEvent(sender, invokeMessage));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
