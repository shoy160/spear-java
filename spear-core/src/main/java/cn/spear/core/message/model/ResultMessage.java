package cn.spear.core.message.model;

import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.TypeUtils;

/**
 * @author shay
 * @date 2020/9/4
 */
public interface ResultMessage<T> extends Message {
    /**
     * 获取状态码
     *
     * @return code
     */
    Integer getCode();

    /**
     * 设置编码
     *
     * @param code code
     */
    void setCode(Integer code);

    /**
     * 获取异常消息
     *
     * @return message
     */
    String getMessage();

    /**
     * 设置错误消息
     *
     * @param message 消息
     */
    void setMessage(String message);

    /**
     * 获取消息实体
     *
     * @return content
     */
    T getContent();

    /**
     * 设置结果内容
     *
     * @param content 内容
     */
    void setContent(T content);

    /**
     * 获取泛型实例
     *
     * @return T
     */
    default T createInstance() {
        return TypeUtils.createGenericInstance(this.getClass());
    }
}
