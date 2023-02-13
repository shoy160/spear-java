package cn.spear.nacos.route;

import cn.spear.core.service.ServiceAddress;
import cn.spear.core.service.ServiceFinder;
import cn.spear.core.service.ServiceRegister;
import cn.spear.core.service.enums.ServiceCodec;
import cn.spear.core.service.enums.ServiceProtocol;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.MapUtils;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shay
 * @date 2020/12/1
 */
public class NacosServiceRoute implements ServiceRegister, ServiceFinder {
    private final String addr;
    private final String clusters;
    private final static String KEY_SERVER_ADDR = "nacos_addr";
    private final static String KEY_CLUSTER_NAME = "nacos_ns";
    private final static String KEY_PROTOCOL = "protocol";
    private final static String KEY_GZIP = "gzip";
    private final static String KEY_CODEC = "codec";
    private final ConcurrentMap<String, List<ServiceAddress>> serviceCache;

    public NacosServiceRoute(String addr, String clusters) {
        this.addr = addr;
        this.clusters = clusters;
        serviceCache = new ConcurrentHashMap<>();
    }

    public NacosServiceRoute() {
        this.clusters = System.getProperty(KEY_CLUSTER_NAME);
        this.addr = System.getProperty(KEY_SERVER_ADDR);
        serviceCache = new ConcurrentHashMap<>();
    }

    @Override
    public List<ServiceAddress> find(String serviceName) {
        try {
            if (serviceCache.containsKey(serviceName)) {
                return serviceCache.getOrDefault(serviceName, new ArrayList<>());
            }
            NamingService namingService = NamingFactory.createNamingService(this.addr);
            List<Instance> instances;
            if (CommonUtils.isNotEmpty(this.clusters)) {
                instances = namingService.selectInstances(serviceName, Arrays.asList(this.clusters.split("[,;]")), true);
            } else {
                instances = namingService.selectInstances(serviceName, true);
            }

            List<ServiceAddress> addressList = new ArrayList<>();
            for (Instance instance : instances) {
                ServiceAddress address = new ServiceAddress(instance.getIp(), instance.getPort());
                address.setService(instance.getIp());
                address.setWeight((int) instance.getWeight());
                Map<String, String> metadata = instance.getMetadata();
                String protocol = MapUtils.get(metadata, KEY_PROTOCOL, null);
                ServiceProtocol serviceProtocol = CommonUtils.isEmpty(protocol) ? ServiceProtocol.Tcp : ServiceProtocol.valueOf(protocol);
                address.setProtocol(serviceProtocol);
                String gzip = MapUtils.get(metadata, KEY_GZIP, null);
                address.setGzip("true".equals(gzip));
                String codec = MapUtils.get(metadata, KEY_CODEC, null);
                ServiceCodec serviceCodec = CommonUtils.isEmpty(codec) ? ServiceCodec.Json : ServiceCodec.valueOf(codec);
                address.setCodec(serviceCodec);
                addressList.add(address);
            }
            serviceCache.putIfAbsent(serviceName, addressList);
            return addressList;
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clean(String serviceName) {
        serviceCache.clear();
    }

    @Override
    public void regist(String serviceName, ServiceAddress address) {
        try {
            Instance instance = new Instance();
            instance.setClusterName(clusters);
            instance.setIp(address.getService());
            instance.setPort(address.getPort());
            instance.setWeight(address.getWeight());
            Map<String, String> instanceMeta = new HashMap<>(3);
            instanceMeta.put(KEY_PROTOCOL, address.getProtocol().toString());
            instanceMeta.put(KEY_GZIP, address.getGzip().toString());
            instanceMeta.put(KEY_CODEC, address.getCodec().toString());
            instance.setMetadata(instanceMeta);
            NamingService namingService = NamingFactory.createNamingService(this.addr);
            namingService.registerInstance(serviceName, instance);
            if (serviceCache.containsKey(serviceName)) {
                List<ServiceAddress> addressList = serviceCache.get(serviceName);
                addressList.add(address);
                serviceCache.replace(serviceName, addressList);
            } else {
                serviceCache.putIfAbsent(serviceName, new ArrayList<ServiceAddress>() {{
                    add(address);
                }});
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deregist() {
        if (CommonUtils.isEmpty(serviceCache)) {
            return;
        }
        try {
            NamingService namingService = NamingFactory.createNamingService(this.addr);
            for (String key : serviceCache.keySet()) {
                for (ServiceAddress address : serviceCache.get(key)) {
                    try {

                        namingService.deregisterInstance(key, address.getService(), address.getPort(), clusters);
                    } catch (NacosException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
        serviceCache.clear();
    }
}
