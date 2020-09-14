package cn.spear.simple;

import cn.spear.codec.json.JsonMessageCodec;
import cn.spear.core.ioc.ServiceProvider;
import cn.spear.core.service.ServiceBuilder;
import cn.spear.core.service.ServiceClientFactory;
import cn.spear.core.service.ServiceExecutor;
import cn.spear.core.service.enums.ServiceProtocol;
import cn.spear.core.service.impl.DefaultServiceBuilder;
import cn.spear.core.service.impl.DefaultServiceRouter;
import cn.spear.protocol.tcp.TcpClientFactory;

/**
 * @author shay
 * @date 2020/9/14
 */
public class SpearClient {
    public static void main(String[] args) {
        ServiceBuilder builder = DefaultServiceBuilder.newBuilder();
        builder.addCodec(JsonMessageCodec.class)
                .addProtocol(ServiceProtocol.Tcp)
                .addRoute(DefaultServiceRouter.class)
                .addSpearClient(b -> {
                    b.addSingleton(ServiceClientFactory.class, p -> {
                        ServiceExecutor executor = p.getServiceT(ServiceExecutor.class);
                        return new TcpClientFactory(executor);
                    });
                });
        ServiceProvider provider = builder.build();

    }
}
