package cn.spear.core.service.impl;

import cn.spear.core.ioc.impl.ServiceCollectionImpl;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.service.ServiceBuilder;
import cn.spear.core.service.ServiceFinder;
import cn.spear.core.service.ServiceRegister;
import cn.spear.core.service.enums.ServiceProtocol;

/**
 * 服务构建器
 *
 * @author shay
 * @date 2020/9/8
 */
public class ServiceBuilderImpl extends ServiceCollectionImpl implements ServiceBuilder {

    @Override
    public <T extends MessageCodec> ServiceBuilder addCodec(Class<T> clazz) {
        addSingleton(MessageCodec.class, clazz);
        return this;
    }

    @Override
    public <TRegister extends ServiceRegister, TFinder extends ServiceFinder> ServiceBuilder addRoute(Class<TRegister> registerType, Class<TFinder> finderType) {
        addSingleton(ServiceRegister.class, registerType);
        addSingleton(ServiceFinder.class, finderType);
        return this;
    }

    @Override
    public ServiceBuilder addProtocol(ServiceProtocol protocol) {
        return this;
    }
}
