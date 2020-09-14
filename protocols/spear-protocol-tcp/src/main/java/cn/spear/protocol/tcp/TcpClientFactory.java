package cn.spear.protocol.tcp;

import cn.spear.core.ioc.IocContext;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.impl.DefaultMessageListener;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceClient;
import cn.spear.core.service.ServiceExecutor;
import cn.spear.core.service.impl.BaseServiceClientFactory;
import cn.spear.core.service.impl.DefaultServiceClient;
import cn.spear.protocol.tcp.handler.MessageHandler;
import cn.spear.protocol.tcp.sender.TcpClientSender;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.AttributeKey;

/**
 * @author shay
 * @date 2020/9/11
 */
public class TcpClientFactory extends BaseServiceClientFactory {
    private final ServiceExecutor executor;
    private final AttributeKey<ServiceAddress> ADDRESS_KEY = AttributeKey.valueOf("address");
    private final AttributeKey<MessageSender> SENDER_KEY = AttributeKey.valueOf("sender");
    private final AttributeKey<MessageListener> LISTENER_KEY = AttributeKey.valueOf("listener");

    public TcpClientFactory(ServiceExecutor executor) {
        this.executor = executor;
    }

    private Bootstrap createBootstrap(MessageCodec codec, boolean gzip) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .channel(NioServerSocketChannel.class)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .group(group)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new LengthFieldPrepender(4))
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                .addLast(new MessageHandler<DefaultResultMessage>(codec, gzip))
                                .addLast(new SimpleChannelInboundHandler<DefaultResultMessage>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext context, DefaultResultMessage message) throws Exception {
                                        MessageListener listener = context.channel().attr(LISTENER_KEY).get();
                                        MessageSender sender = context.channel().attr(SENDER_KEY).get();
                                        listener.onReceived(new MessageEvent(sender, message));
                                    }

                                    @Override
                                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                        super.channelInactive(ctx);
                                        ServiceAddress address = ctx.channel().attr(ADDRESS_KEY).get();
                                        removeClient(address);
                                    }
                                });
                    }
                });
        return bootstrap;
    }

    @Override
    protected ServiceClient createClient(ServiceAddress address) {
        MessageCodec codec = IocContext.getServiceT(MessageCodec.class);
        Bootstrap bootstrap = createBootstrap(codec, address.getGzip());
        ChannelFuture connect = bootstrap.connect(address.getClientAddress());
        Channel channel = connect.channel();
        DefaultMessageListener listener = new DefaultMessageListener();
        TcpClientSender sender = new TcpClientSender(codec, channel, address);
        channel.attr(ADDRESS_KEY).set(address);
        channel.attr(SENDER_KEY).set(sender);
        channel.attr(LISTENER_KEY).set(listener);
        return new DefaultServiceClient(sender, listener, this.executor);
    }
}
