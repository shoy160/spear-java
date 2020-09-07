package cn.spear.core.message;

import java.util.Objects;

/**
 * 消息序列化
 *
 * @author shay
 * @date 2020/9/7
 */
public interface MessageSerializer {
    /**
     * 序列化对象
     *
     * @param value 对象值
     * @return byte[]
     */
    byte[] serialize(Object value);

    /**
     * 序列化匿名对象
     *
     * @param value 对象值
     * @return byte[]
     */
    default byte[] serializeNoType(Object value) {
        return serialize(value);
    }

    /**
     * 反序列化数据
     *
     * @param data byte[]
     * @param type 对象类型
     * @return 对象值
     */
    Object deserialize(byte[] data, Class<?> type);

    /**
     * 反序列化匿名数据
     *
     * @param data byte[]
     * @param type 对象类型
     * @return 对象值
     */
    default Object deserializeNoType(byte[] data, Class<?> type) {
        return deserialize(data, type);
    }

    /**
     * 反序列化匿名数据
     *
     * @param data byte[]
     * @param type 对象类型
     * @param <T>  T
     * @return 对象值
     */
    default <T> T deserializeT(byte[] data, Class<T> type) {
        Object msg = deserialize(data, type);
        if (msg == null) {
            return null;
        }
        // todo 类型转换
        return (T) msg;
    }
}
