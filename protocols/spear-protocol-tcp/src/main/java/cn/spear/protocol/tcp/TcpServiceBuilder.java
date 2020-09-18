package cn.spear.protocol.tcp;

import cn.spear.core.message.MessageCodec;
import cn.spear.core.service.ServiceBuilder;
import cn.spear.core.service.ServiceClientFactory;
import cn.spear.core.service.ServiceExecutor;
import cn.spear.core.service.ServiceListener;
import cn.spear.core.service.enums.ServiceProtocol;

/**
 * @author shay
 * @date 2020/9/18
 */
public class TcpServiceBuilder {
    /**
     * 添加TCP协议
     *
     * @param builder builder
     */
    public static void addTcpProtocol(ServiceBuilder builder) {
        builder.addProtocol(ServiceProtocol.Tcp);
        builder.addSingleton(ServiceListener.class, p -> {
            MessageCodec codec = p.getServiceT(MessageCodec.class);
            return new TcpServiceListener(codec);
        });
    }

    /**
     * 添加TCP协议客户端
     *
     * @param builder builder
     */
    public static void addTcpProtocolClient(ServiceBuilder builder) {
        builder.addProtocol(ServiceProtocol.Tcp);
        builder.addSingleton(ServiceClientFactory.class, p -> {
            ServiceExecutor executor = p.getServiceT(ServiceExecutor.class);
            return new TcpClientFactory(executor);
        });
    }
}
