package cn.spear.core.service.impl;

import cn.spear.core.ioc.IocContext;
import cn.spear.core.service.*;
import cn.spear.core.service.enums.ServiceCodec;
import cn.spear.core.service.enums.ServiceProtocol;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shay
 * @date 2020/9/8
 */
@Slf4j
public class DefaultServiceHost extends BaseServiceHost {

    private final ServiceRegister register;
    private final ServiceEntryFactory entryFactory;
    private final ServiceListener listener;

    public DefaultServiceHost(
            ServiceExecutor executor, ServiceRegister register,
            ServiceEntryFactory entryFactory, ServiceListener listener
    ) {
        super(executor, listener);
        this.register = register;
        this.entryFactory = entryFactory;
        this.listener = listener;
    }

    @Override
    public void start(ServiceAddress address) {
        try {
            ServiceProtocol protocol = IocContext.getServiceT(ServiceProtocol.class);
            ServiceCodec codec = IocContext.getServiceT(ServiceCodec.class);
            address.setProtocol(protocol);
            address.setCodec(codec);
            Thread t = new Thread(() -> listener.start(address));
            t.start();
            log.info("服务已启动:{},Gzip:{},Codec:{},Protocol:{}", address, address.getGzip(), address.getCodec(), address.getProtocol());
        } catch (Exception ex) {
            log.error("服务启动失败", ex);
        }
        //服务注册
        List<Class<?>> services = this.entryFactory.getServices();
        this.register.regist(services, address);
    }

    @Override
    public void close() throws Exception {
        this.register.deregist();
        this.listener.close();
        log.info("服务已停止");
    }
}
