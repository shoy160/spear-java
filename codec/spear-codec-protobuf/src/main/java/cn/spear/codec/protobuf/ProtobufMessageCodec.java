package cn.spear.codec.protobuf;

import cn.spear.codec.protobuf.model.ProtobufDynamicMessage;
import cn.spear.codec.protobuf.model.ProtobufInvokeMessage;
import cn.spear.codec.protobuf.model.ProtobufResultMessage;
import cn.spear.core.message.impl.BaseMessageCodec;
import cn.spear.core.service.annotation.SpearConf;
import cn.spear.core.service.enums.ServiceCodec;

/**
 * @author shay
 * @date 2020/9/11
 */
@SpearConf(codec = ServiceCodec.ProtoBuf)
public class ProtobufMessageCodec extends BaseMessageCodec<ProtobufDynamicMessage, ProtobufInvokeMessage, ProtobufResultMessage> {
    public ProtobufMessageCodec() {
        super(new ProtobufMessageSerializer());
    }
}
