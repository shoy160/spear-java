package cn.spear.codec.json;

import cn.spear.codec.json.model.JsonDynamicMessage;
import cn.spear.codec.json.model.JsonInvokeMessage;
import cn.spear.codec.json.model.JsonResultMessage;
import cn.spear.core.message.MessageSerializer;
import cn.spear.core.message.impl.BaseMessageCodec;
import cn.spear.core.service.annotation.SpearConf;
import cn.spear.core.service.enums.ServiceCodec;

/**
 * Json编解码器
 *
 * @author shay
 * @date 2020/9/7
 */
@SpearConf(codec = ServiceCodec.Json)
public class JsonMessageCodec extends BaseMessageCodec<JsonDynamicMessage, JsonInvokeMessage, JsonResultMessage> {
    public JsonMessageCodec() {
        super(new JsonMessageSerializer());
    }
}
