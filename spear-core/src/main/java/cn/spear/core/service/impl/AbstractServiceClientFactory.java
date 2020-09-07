package cn.spear.core.service.impl;

import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceClient;
import cn.spear.core.service.ServiceClientFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端工厂抽象类
 *
 * @author shay
 * @date 2020/9/4
 */
public abstract class AbstractServiceClientFactory implements ServiceClientFactory {
    private final ConcurrentHashMap<ServiceAddress, ServiceClient> clients;

    protected AbstractServiceClientFactory() {
        clients = new ConcurrentHashMap<>();
    }

    /**
     * 创建客户端
     *
     * @param address 客户端地址
     * @return 客户端
     */
    protected abstract ServiceClient createClient(ServiceAddress address);

    @Override
    public ServiceClient create(ServiceAddress address) {
        ServiceClient client = clients.get(address);
        if (client == null) {
            client = createClient(address);
            client = clients.putIfAbsent(address, client);
        }
        return client;
    }

    @Override
    public void close() {
        for (ServiceClient client : clients.values()) {
            client.close();
        }
        clients.clear();
    }
}
