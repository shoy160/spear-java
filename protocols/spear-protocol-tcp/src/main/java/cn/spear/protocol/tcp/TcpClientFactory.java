package cn.spear.protocol.tcp;

import cn.spear.core.ioc.IocContext;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.impl.DefaultMessageListener;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceClient;
import cn.spear.core.service.ServiceExecutor;
import cn.spear.core.service.impl.BaseServiceClientFactory;
import cn.spear.core.service.impl.DefaultServiceClient;
import cn.spear.protocol.tcp.handler.ClientHandler;
import cn.spear.protocol.tcp.handler.MessageHandler;
import cn.spear.protocol.tcp.sender.TcpClientSender;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author shay
 * @date 2020/9/11
 */
public class TcpClientFactory extends BaseServiceClientFactory {
    private final ServiceExecutor executor;

    public TcpClientFactory(ServiceExecutor executor) {
        this.executor = executor;
    }
    public TcpClientFactory(ServiceExecutor executor, Integer maxPool) {
        super(maxPool);
        this.executor = executor;
    }

    private Bootstrap createBootstrap(MessageCodec codec, boolean gzip) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
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
                                .addLast(new MessageHandler<>(codec, gzip, DefaultResultMessage.class))
                                .addLast(new ClientHandler(address -> removeClient(address)));
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
        channel.attr(TcpAttributes.ADDRESS_KEY).set(address);
        channel.attr(TcpAttributes.SENDER_KEY).set(sender);
        channel.attr(TcpAttributes.LISTENER_KEY).set(listener);
        return new DefaultServiceClient(sender, listener, this.executor);
    }
}
