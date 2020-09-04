package cn.spear.core.message;

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
    int getCode();

    /**
     * 获取异常消息
     *
     * @return message
     */
    String getMessage();

    /**
     * 获取消息实体
     *
     * @return content
     */
    T getContent();
}
