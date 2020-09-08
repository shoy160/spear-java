package cn.spear.codec.json;

import cn.spear.codec.json.model.JsonDynamicMessage;
import cn.spear.codec.json.model.JsonInvokeMessage;
import cn.spear.codec.json.model.JsonResultMessage;
import cn.spear.core.message.MessageSerializer;
import cn.spear.core.message.impl.BaseMessageCodec;

/**
 * Json编解码器
 *
 * @author shay
 * @date 2020/9/7
 */
public class JsonMessageCodec extends BaseMessageCodec<JsonDynamicMessage, JsonInvokeMessage, JsonResultMessage> {
    public JsonMessageCodec(MessageSerializer serializer) {
        super(serializer);
    }
}
