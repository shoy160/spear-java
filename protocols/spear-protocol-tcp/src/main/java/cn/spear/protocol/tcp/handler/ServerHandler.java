package cn.spear.protocol.tcp.handler;

import cn.spear.core.lang.Action;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.service.ServiceAddress;
import cn.spear.protocol.tcp.sender.TcpServerSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shay
 * @date 2020/9/14
 */
@Slf4j
@RequiredArgsConstructor
public class ServerHandler extends SimpleChannelInboundHandler<DefaultInvokeMessage> {
    private final ServiceAddress address;
    private final MessageCodec codec;
    private final Action<MessageEvent> receivedAction;

    @Override
    protected void channelRead0(ChannelHandlerContext context, DefaultInvokeMessage invokeMessage) {
        TcpServerSender sender = new TcpServerSender(this.codec, context, this.address);
        if (null != receivedAction) {
            this.receivedAction.invoke(new MessageEvent(sender, invokeMessage));
        }
    }
}
