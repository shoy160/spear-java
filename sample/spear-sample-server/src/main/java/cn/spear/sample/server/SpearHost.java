package cn.spear.sample.server;

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
//                        .addRoute(new NacosServiceRoute("60.255.161.101:8848", "public"))
                        .addServer(TcpServiceBuilder::addTcpProtocol, "cn.spear.sample")
                        .build();

        ServiceHost host = provider.getServiceT(ServiceHost.class);

        ServiceAddress address = new ServiceAddress(9501);
        host.start(address);
    }
}
