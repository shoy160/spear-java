package cn.spear.sample;

import cn.spear.codec.json.JsonMessageCodec;
import cn.spear.core.ioc.ServiceProvider;
import cn.spear.core.proxy.ProxyFactory;
import cn.spear.core.service.*;
import cn.spear.core.service.impl.DefaultServiceBuilder;
import cn.spear.core.service.impl.DefaultServiceRouter;
import cn.spear.nacos.route.NacosServiceRoute;
import cn.spear.protocol.tcp.TcpServiceBuilder;
import cn.spear.sample.contract.UserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shay
 * @date 2020/9/14
 */
public class SpearClient {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SpearClient.class);

        DefaultServiceRouter router = new DefaultServiceRouter();
        router.regist("sample-service", new ServiceAddress("127.0.0.1", 9501));

        ServiceProvider provider =
                DefaultServiceBuilder.newBuilder()
                        .addCodec(JsonMessageCodec.class)
                        .addRoute(router)
//                        .addRoute(new NacosServiceRoute("60.255.161.101:8848", "public"))
                        .addClient(TcpServiceBuilder::addTcpProtocolClient)
                        .build();

        long time = System.currentTimeMillis();
        int count = 1;
        for (int i = 0; i < count; i++) {
            try {
                ProxyFactory proxyFactory = provider.getServiceT(ProxyFactory.class);
                UserClient client = proxyFactory.createT(UserClient.class, 2);
//            String shay = client.hello("shay");
//            logger.info("hello result:{}", shay);
                client.add("shay001");
//                client.add(18);
//                List<UserDTO> list = client.search(new UserSearchDTO());
//                byte[] encode = codec.encode(list, false);
//                logger.debug("search result size:{}", list.size());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        long ms = System.currentTimeMillis() - time;
        logger.info("invoke {} count,use {} ms,tps:{}", count, ms, count * 1000D / ms);
    }
}
