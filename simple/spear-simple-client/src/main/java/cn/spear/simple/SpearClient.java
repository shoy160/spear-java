package cn.spear.simple;

import cn.spear.codec.json.JsonMessageCodec;
import cn.spear.core.ioc.ServiceProvider;
import cn.spear.core.proxy.ProxyFactory;
import cn.spear.core.service.*;
import cn.spear.core.service.impl.DefaultServiceBuilder;
import cn.spear.core.service.impl.DefaultServiceRouter;
import cn.spear.protocol.tcp.TcpServiceBuilder;
import cn.spear.simple.contract.UserClient;
import cn.spear.simple.contract.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author shay
 * @date 2020/9/14
 */
public class SpearClient {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SpearClient.class);

        DefaultServiceRouter router = new DefaultServiceRouter();
        router.regist("simple-service", new ServiceAddress("127.0.0.1", 9501));

        ServiceProvider provider =
                DefaultServiceBuilder.newBuilder()
                        .addCodec(JsonMessageCodec.class)
                        .addRoute(router)
                        .addClient(TcpServiceBuilder::addTcpProtocolClient)
                        .build();

        long time = System.currentTimeMillis();
        int count = 10000;
        for (int i = 0; i < count; i++) {
            try {
                ProxyFactory proxyFactory = provider.getServiceT(ProxyFactory.class);
                UserClient client = proxyFactory.createT(UserClient.class, 2);
//            String shay = client.hello("shay");
//            logger.info("hello result:{}", shay);
                client.add("shay");
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
