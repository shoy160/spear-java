package cn.spear.protocol.tcp;

import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.event.MessageEvent;
import cn.spear.core.message.impl.DefaultMessageListener;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceListener;
import cn.spear.protocol.tcp.handler.MessageHandler;
import cn.spear.protocol.tcp.sender.TcpServerSender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shay
 * @date 2020/9/11
 */
@Slf4j
public class TcpServiceListener extends DefaultMessageListener implements ServiceListener {
    private Channel channel;
    private final MessageCodec codec;

    public TcpServiceListener(MessageCodec codec) {
        this.codec = codec;
    }

    @Override
    public void start(ServiceAddress address) {
        log.debug("ready to listen at:{}", address.toString());

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.SO_BACKLOG, 8192)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new LengthFieldPrepender(4))
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                .addLast(new MessageHandler<DefaultInvokeMessage>(codec, address.getGzip()))
                                .addLast(new SimpleChannelInboundHandler<DefaultInvokeMessage>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext context, DefaultInvokeMessage invokeMessage) {
                                        TcpServerSender sender = new TcpServerSender(codec, context, address);
                                        onReceived(new MessageEvent(sender, invokeMessage));
                                    }
                                })
                        ;
                    }
                });

        ChannelFuture future = bootstrap.bind(address.getServerAddress());
        this.channel = future.channel();
        try {
            this.channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (null != this.channel && this.channel.isOpen()) {
            this.channel.disconnect();
        }
    }
}
