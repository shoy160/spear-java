package cn.spear.codec.protobuf.model;

import cn.spear.codec.protobuf.ProtobufMessageSerializer;
import cn.spear.core.message.model.impl.BaseDynamicMessage;

/**
 * @author shay
 * @date 2020/9/11
 */
public class ProtobufDynamicMessage extends BaseDynamicMessage {
    public ProtobufDynamicMessage() {
        super(new ProtobufMessageSerializer());
    }
}
