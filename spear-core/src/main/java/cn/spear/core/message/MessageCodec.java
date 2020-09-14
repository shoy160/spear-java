package cn.spear.core.message;

import cn.spear.core.util.CommonUtils;

/**
 * 消息编解码器
 *
 * @author shay
 * @date 2020/9/4
 */
public interface MessageCodec {
    /**
     * 消息编码
     *
     * @param message 消息体
     * @param gzip    是否开启Gzip压缩
     * @return byte[]
     */
    byte[] encode(Object message, boolean gzip);

    /**
     * 消息编码
     *
     * @param message 消息体
     * @return byte[]
     */
    default byte[] encode(Object message) {
        return encode(message, true);
    }

    /**
     * 消息解码
     *
     * @param data  数据byte[]
     * @param clazz 类型
     * @param gzip  是否开启Gzip压缩
     * @return Object
     */
    Object decode(byte[] data, Class<?> clazz, boolean gzip);

    /**
     * 消息解码
     *
     * @param data  数据byte[]
     * @param clazz 类型
     * @return Object
     */
    default Object decode(byte[] data, Class<?> clazz) {
        return decode(data, clazz, true);
    }

    default <T> T decodeT(byte[] data, Class<T> clazz) {
        return decodeT(data, clazz, true);
    }

    /**
     * 消息解码
     *
     * @param data  数据byte[]
     * @param clazz 类型
     * @return Object
     */
    default <T> T decodeT(byte[] data, Class<T> clazz, boolean gzip) {
        Object message = decode(data, clazz, gzip);
        if (message == null) {
            return null;
        }
        return CommonUtils.cast(message, clazz);
    }
}
