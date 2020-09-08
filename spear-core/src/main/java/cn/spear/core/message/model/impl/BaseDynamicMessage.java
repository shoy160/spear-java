package cn.spear.core.message.model.impl;

import cn.spear.core.message.MessageSerializer;
import cn.spear.core.message.model.DynamicMessage;
import cn.spear.core.util.CommonUtils;

/**
 * @author shay
 * @date 2020/9/7
 */
public class BaseDynamicMessage implements DynamicMessage {
    private String contentType;
    private byte[] content;
    private final MessageSerializer serializer;

    public BaseDynamicMessage(MessageSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public byte[] getContent() {
        return this.content;
    }

    @Override
    public void initValue(Object value) {
        if (value == null) {
            return;
        }
        this.contentType = value.getClass().getName();
        this.content = this.serializer.serialize(value);
    }

    @Override
    public Object value() {
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
