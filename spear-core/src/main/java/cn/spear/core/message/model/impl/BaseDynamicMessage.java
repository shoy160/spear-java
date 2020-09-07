package cn.spear.core.message.model.impl;

import cn.spear.core.message.MessageSerializer;
import cn.spear.core.message.model.DynamicMessage;
import cn.spear.core.util.CommonUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shay
 * @date 2020/9/7
 */
@Getter
@Setter
public class BaseDynamicMessage implements DynamicMessage {
    private String contentType;
    private byte[] content;
    private MessageSerializer serializer;

    public BaseDynamicMessage() {
    }

    public BaseDynamicMessage(MessageSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            return;
        }
        this.contentType = value.getClass().getName();
        this.content = this.serializer.serialize(value);
    }

    @Override
    public Object getValue() {
        if (this.content == null || CommonUtils.isEmpty(this.contentType)) {
            return null;
        }
        try {
            Class<?> type = Class.forName(this.contentType);
            return this.serializer.deserialize(this.content, type);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
