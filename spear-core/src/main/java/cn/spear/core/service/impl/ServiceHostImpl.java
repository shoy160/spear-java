package cn.spear.core.service.impl;

import cn.spear.core.service.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shay
 * @date 2020/9/8
 */
@Slf4j
public class ServiceHostImpl extends BaseServiceHost {

    private final ServiceRegister register;
    private final ServiceEntryFactory entryFactory;
    private final ServiceListener listener;

    public ServiceHostImpl(ServiceExecutor executor, ServiceRegister register, ServiceEntryFactory entryFactory, ServiceListener listener) {
        super(executor);
        this.register = register;
        this.entryFactory = entryFactory;
        this.listener = listener;
    }

    @Override
    public void start(ServiceAddress address) {
        try {
            Thread t = new Thread(() -> listener.start(address));
            t.start();
        } catch (Exception ex) {
            log.error("服务启动失败", ex);
        }
        //服务注册
        List<Class<?>> services = this.entryFactory.getServices();
        this.register.regist(services, address);
    }

    @Override
    public void stop() {
        this.register.deregist();
        this.listener.stop();
    }
}
