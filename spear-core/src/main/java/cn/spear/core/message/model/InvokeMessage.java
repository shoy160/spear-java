package cn.spear.core.message.model;

import cn.spear.core.util.TypeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用消息
 *
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
     * 添加参数
     *
     * @param key   key
     * @param value value
     */
    default void addParameter(String key, T value) {
        Map<String, T> parameters = this.getParameters();
        if (parameters == null) {
            parameters = new HashMap<>(1);
            parameters.put(key, value);
            this.setParameters(parameters);
            return;
        }
        if (parameters.containsKey(key)) {
            parameters.replace(key, value);
        } else {
            parameters.put(key, value);
        }
    }

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
     * 添加Header
     *
     * @param key   key
     * @param value value
     */
    default void addHeader(String key, String value) {
        Map<String, String> headers = this.getHeaders();
        if (headers == null) {
            headers = new HashMap<>(1);
            headers.put(key, value);
            this.setHeaders(headers);
        } else {
            if (headers.containsKey(key)) {
                headers.replace(key, value);
            } else {
                headers.put(key, value);
            }
        }
    }

    /**
     * 获取泛型实例
     *
     * @return T
     */
    default T createInstance() {
        return TypeUtils.createGenericInstance(this.getClass());
    }
}
