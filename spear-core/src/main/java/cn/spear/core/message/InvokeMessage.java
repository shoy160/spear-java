package cn.spear.core.message;

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
     * 获取参数
     *
     * @return map
     */
    Map<String, T> getParameters();

    /**
     * 获取header
     *
     * @return map
     */
    Map<String, String> getHeaders();
}
