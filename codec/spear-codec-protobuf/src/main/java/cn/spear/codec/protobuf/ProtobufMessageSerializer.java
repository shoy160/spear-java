package cn.spear.codec.protobuf;

import cn.spear.core.message.MessageSerializer;

import java.lang.reflect.Type;

/**
 * @AUTHOR SHAY
 * @DATE 2020/9/11
 */
public class ProtobufMessageSerializer implements MessageSerializer {
    @Override
    public byte[] serialize(Object value) {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] data, Type type) {
        return null;
    }
}
