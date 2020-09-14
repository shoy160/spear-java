package cn.spear.simple;

import cn.spear.codec.json.JsonMessageCodec;
import cn.spear.core.ioc.ServiceProvider;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceBuilder;
import cn.spear.core.service.ServiceHost;
import cn.spear.core.service.ServiceListener;
import cn.spear.core.service.enums.ServiceProtocol;
import cn.spear.core.service.impl.DefaultServiceBuilder;
import cn.spear.core.service.impl.DefaultServiceRouter;
import cn.spear.protocol.tcp.TcpServiceListener;

/**
 * @author shay
 * @date 2020/9/8
 */
public class SpearHost {

    public static void main(String[] args) {
        ServiceBuilder builder = DefaultServiceBuilder.newBuilder();
        ServiceProvider provider = builder
                .addCodec(JsonMessageCodec.class)
                .addRoute(DefaultServiceRouter.class)
                .addProtocol(ServiceProtocol.Tcp)
                .addSpearServer(b -> {
                    b.addSingleton(ServiceListener.class, p -> {
                        MessageCodec codec = p.getServiceT(MessageCodec.class);
                        return new TcpServiceListener(codec);
                    });
                })
                .build();

        ServiceHost host = provider.getServiceT(ServiceHost.class);

        ServiceAddress address = new ServiceAddress("127.0.0.1", 9501);
        address.setGzip(true);
        address.setService("192.168.2.54");
        host.start(address);
    }
}
