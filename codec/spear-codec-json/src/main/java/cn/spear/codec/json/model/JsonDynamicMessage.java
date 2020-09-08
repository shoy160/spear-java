package cn.spear.codec.json.model;

import cn.spear.codec.json.JsonMessageSerializer;
import cn.spear.core.message.model.impl.BaseDynamicMessage;

/**
 * @author shay
 * @date 2020/9/7
 */
public class JsonDynamicMessage extends BaseDynamicMessage {

    public JsonDynamicMessage() {
        super(new JsonMessageSerializer());
    }
}
