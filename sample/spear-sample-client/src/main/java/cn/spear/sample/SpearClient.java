package cn.spear.sample;

import cn.spear.codec.json.JsonMessageCodec;
import cn.spear.core.ioc.ServiceProvider;
import cn.spear.core.proxy.ProxyFactory;
import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceClientFactory;
import cn.spear.core.service.impl.DefaultServiceBuilder;
import cn.spear.core.service.impl.DefaultServiceRouter;
import cn.spear.protocol.tcp.TcpServiceBuilder;
import cn.spear.sample.contract.UserClient;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shay
 * @date 2020/9/14
 */
public class SpearClient {
    @SneakyThrows
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SpearClient.class);

        DefaultServiceRouter router = new DefaultServiceRouter();
        ServiceAddress address = new ServiceAddress("127.0.0.1", 9501);
        router.regist("sample-service", address);

        ServiceProvider provider =
                DefaultServiceBuilder.newBuilder()
                        .addCodec(JsonMessageCodec.class)
                        .addRoute(router)
                        .addClient(builder ->
                                TcpServiceBuilder.addTcpProtocolClient(builder, 1))

//                        .addRoute(new NacosServiceRoute("60.255.161.101:8848", "public"))
                        .addClient(TcpServiceBuilder::addTcpProtocolClient)
                        .build();
        batchTest(provider, logger);
        provider.getServiceT(ServiceClientFactory.class).close();
    }

    private static void singleTest(ServiceProvider provider, Logger logger) {
        ProxyFactory proxyFactory = provider.getServiceT(ProxyFactory.class);
        UserClient client = proxyFactory.createT(UserClient.class, 2);
        String result = client.hello("world");
        logger.info(result);
    }

    private static void batchTest(ServiceProvider provider, Logger logger) {
        ProxyFactory proxyFactory = provider.getServiceT(ProxyFactory.class);
        UserClient client = proxyFactory.createT(UserClient.class);
//        MessageCodec codec = provider.getServiceT(MessageCodec.class);
        client.hello("shay");
        long time = System.currentTimeMillis();
        int count = 10000;
        for (int i = 0; i < count; i++) {
            try {
//                String shay = client.hello("shay");
//                logger.info("hello result:{}", shay);
                client.add("shay");
//                client.add(18);
//                UserSearchDTO searchDTO = new UserSearchDTO();
//                searchDTO.setName("05");
//                List<UserDTO> list = client.search(searchDTO);
//                byte[] encode = codec.encode(list, false);
//                logger.info("search result:{}", new String(encode));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        long ms = System.currentTimeMillis() - time;
        logger.info("invoke {} count,use {} ms,tps:{}", count, ms, count * 1000D / ms);
    }
}
