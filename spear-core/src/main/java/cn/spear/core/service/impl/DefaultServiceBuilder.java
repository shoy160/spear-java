package cn.spear.core.service.impl;

import cn.spear.core.ioc.ServiceLifetime;
import cn.spear.core.ioc.impl.ServiceCollectionImpl;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.MessageListener;
import cn.spear.core.message.impl.DefaultMessageListener;
import cn.spear.core.service.ServiceBuilder;
import cn.spear.core.service.ServiceFinder;
import cn.spear.core.service.ServiceRegister;
import cn.spear.core.service.annotation.SpearConf;
import cn.spear.core.service.enums.ServiceCodec;
import cn.spear.core.service.enums.ServiceProtocol;

/**
 * 服务构建器
 *
 * @author shay
 * @date 2020/9/8
 */
public class DefaultServiceBuilder extends ServiceCollectionImpl implements ServiceBuilder {

    private DefaultServiceBuilder() {
    }

    public static DefaultServiceBuilder newBuilder() {
        return new DefaultServiceBuilder();
    }

    @Override
    public <T extends MessageCodec> ServiceBuilder addCodec(Class<T> clazz) {
        addSingleton(MessageCodec.class, clazz);
        SpearConf spearConf = clazz.getAnnotation(SpearConf.class);
        if (null != spearConf) {
            add(ServiceCodec.class, p -> spearConf.codec(), ServiceLifetime.Singleton);
        }
        return this;
    }

    @Override
    public <TRegister extends ServiceRegister, TFinder extends ServiceFinder> ServiceBuilder addRoute(Class<TRegister> registerType, Class<TFinder> finderType) {
        addSingleton(MessageListener.class, DefaultMessageListener.class);
        addSingleton(ServiceRegister.class, registerType);
        addSingleton(ServiceFinder.class, finderType);
        return this;
    }

    @Override
    public ServiceBuilder addProtocol(ServiceProtocol protocol) {
        add(ServiceProtocol.class, p -> protocol, ServiceLifetime.Singleton);
        return this;
    }
}
