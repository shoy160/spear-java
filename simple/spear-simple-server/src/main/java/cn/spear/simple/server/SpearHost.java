package cn.spear.simple.server;

import cn.spear.codec.json.JsonMessageCodec;
import cn.spear.core.ioc.ServiceProvider;
import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceHost;
import cn.spear.core.service.impl.DefaultServiceBuilder;
import cn.spear.core.service.impl.DefaultServiceRouter;
import cn.spear.protocol.tcp.TcpServiceBuilder;

/**
 * @author shay
 * @date 2020/9/8
 */
public class SpearHost {

    public static void main(String[] args) {
        ServiceProvider provider =
                DefaultServiceBuilder.newBuilder()
                        .addCodec(JsonMessageCodec.class)
                        .addRoute(DefaultServiceRouter.class)
                        .addServer(TcpServiceBuilder::addTcpProtocol, "cn.spear.simple")
                        .build();

        ServiceHost host = provider.getServiceT(ServiceHost.class);

        ServiceAddress address = new ServiceAddress("127.0.0.1", 9501);
        address.setGzip(true);
        address.setService("192.168.2.54");
        host.start(address);
    }
}
