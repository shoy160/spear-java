package cn.spear.simple;

import cn.spear.codec.json.JsonMessageCodec;
import cn.spear.core.service.ServiceBuilder;
import cn.spear.core.ioc.ServiceProvider;
import cn.spear.core.service.ServiceHost;
import cn.spear.core.service.enums.ServiceProtocol;
import cn.spear.core.service.impl.BaseServiceRouter;
import cn.spear.core.service.impl.ServiceBuilderImpl;

/**
 * @author shay
 * @date 2020/9/8
 */
public class SpearHost {

    public static void main(String[] args) {
        ServiceBuilder builder = new ServiceBuilderImpl();
        ServiceProvider provider = builder
                .addCodec(JsonMessageCodec.class)
                .addRoute(BaseServiceRouter.class)
                .addProtocol(ServiceProtocol.Tcp)
                .build();
        ServiceHost host = provider.getServiceT(ServiceHost.class);
        host.start("127.0.0.1", 9501);
    }
}
