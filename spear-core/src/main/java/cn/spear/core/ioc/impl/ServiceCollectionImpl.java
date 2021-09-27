package cn.spear.core.ioc.impl;

import cn.spear.core.ioc.ServiceCollection;
import cn.spear.core.ioc.ServiceDescriptor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 服务集合实现
 *
 * @author shay
 * @date 2020/9/8
 */
public class ServiceCollectionImpl implements ServiceCollection {
    private final List<ServiceDescriptor> descriptors;

    public ServiceCollectionImpl() {
        this.descriptors = new LinkedList<>();
    }

    @Override
    public ServiceCollection add(ServiceDescriptor descriptor) {
        descriptors.add(descriptor);
        return this;
    }

    @Override
    public List<ServiceDescriptor> getDescriptors() {
        return descriptors;
    }
}
