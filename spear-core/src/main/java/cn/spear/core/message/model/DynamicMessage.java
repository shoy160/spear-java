package cn.spear.core.message.model;

/**
 * 动态消息
 * @author shay
 * @date 2020/9/7
 */
public interface DynamicMessage {
    /**
     * 类型
     *
     * @return 对象类型
     */
    String getContentType();

    /**
     * 获取数据
     *
     * @return 对象数据
     */
    byte[] getContent();

    /**
     * 设置对象
     *
     * @param value 对象
     */
    void initValue(Object value);

    /**
     * 获取对象
     *
     * @return 对象
     */
    Object value();
}
