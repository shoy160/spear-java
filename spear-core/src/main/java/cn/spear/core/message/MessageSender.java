package cn.spear.core.message;

/**
 * 消息发送器
 *
 * @author shay
 * @date 2020/9/4
 */
public interface MessageSender {
    /**
     * 发送消息
     *
     * @param message 消息
     * @param flush   flush
     */
    void send(BaseMessage message, boolean flush);

    /**
     * 发送消息
     *
     * @param message 消息
     */
    default void send(BaseMessage message) {
        send(message, true);
    }
}
