package cn.spear.core.service;

import cn.spear.core.message.model.impl.DefaultInvokeMessage;

import java.lang.reflect.Method;

/**
 * @author shay
 * @date 2020/9/15
 */
public interface ServiceGenerator {
    /**
     * 获取服务ID
     *
     * @param method 方法
     * @return id
     */
    String getServiceId(Method method);

    /**
     * 生成调用方法
     *
     * @param method method
     * @param args   args
     * @return message
     */
    DefaultInvokeMessage createMessage(Method method, Object[] args);
}
