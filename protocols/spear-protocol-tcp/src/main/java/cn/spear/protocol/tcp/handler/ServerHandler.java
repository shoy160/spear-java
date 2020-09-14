package cn.spear.protocol.tcp.handler;

import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shay
 * @date 2020/9/14
 */
public class ServerHandler extends SimpleChannelInboundHandler<DefaultInvokeMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DefaultInvokeMessage invokeMessage) {

    }
}
