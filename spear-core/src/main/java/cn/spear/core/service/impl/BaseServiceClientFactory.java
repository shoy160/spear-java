package cn.spear.core.service.impl;

import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceClient;
import cn.spear.core.service.ServiceClientFactory;
import cn.spear.core.util.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端工厂抽象类
 *
 * @author shay
 * @date 2020/9/4
 */
public abstract class BaseServiceClientFactory implements ServiceClientFactory {
    private final int maxPool;
    private final ConcurrentHashMap<ServiceAddress, List<ServiceClient>> clientCache;
    private final Logger logger;

    protected BaseServiceClientFactory() {
        this(null);
    }

    protected BaseServiceClientFactory(Integer maxPool) {
        clientCache = new ConcurrentHashMap<>();
        this.maxPool = Optional.ofNullable(maxPool).orElse(1);
        this.logger = LoggerFactory.getLogger(BaseServiceClientFactory.class);
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
        List<ServiceClient> clients = clientCache.get(address);
        if (Objects.isNull(clients)) {
            if(logger.isDebugEnabled()) {
                logger.debug("create {} clients for {}", this.maxPool, address);
            }
            clients = new ArrayList<>();
            for (int i = 0; i < this.maxPool; i++) {
                clients.add(createClient(address));
            }
            clientCache.putIfAbsent(address, clients);
        }
        return RandomUtils.randomGet(clients);
    }

    protected void removeClient(ServiceAddress address) {
        this.clientCache.remove(address);
    }

    @Override
    public void close() throws Exception {
        for (List<ServiceClient> clients : clientCache.values()) {
            for (ServiceClient client : clients) {
                client.close();
            }
        }
        clientCache.clear();
    }
}
