package cn.spear.core.message.model;

import cn.spear.core.util.CommonUtils;

import java.util.Map;

/**
 * @author shay
 * @date 2020/9/4
 */
public interface InvokeMessage<T> extends Message {
    /**
     * 获取服务Id
     *
     * @return serviceId
     */
    String getServiceId();

    /**
     * 设置服务ID
     *
     * @param serviceId 服务Id
     */
    void setServiceId(String serviceId);

    /**
     * 获取参数
     *
     * @return map
     */
    Map<String, T> getParameters();

    /**
     * 设置参数
     *
     * @param parameters 参数
     */
    void setParameters(Map<String, T> parameters);

    /**
     * 获取header
     *
     * @return map
     */
    Map<String, String> getHeaders();

    /**
     * 设置请求头
     *
     * @param headers 请求头
     */
    void setHeaders(Map<String, String> headers);

    /**
     * 获取泛型实例
     *
     * @return T
     */
    default T createInstance() {
        return CommonUtils.createGenericInstance(this.getClass());
    }
}
