package cn.spear.core.message.impl;

import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.MessageSerializer;
import cn.spear.core.message.model.InvokeMessage;
import cn.spear.core.message.model.ResultMessage;
import cn.spear.core.message.model.impl.*;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.StreamUtils;

/**
 * 编解码器抽象类
 *
 * @author shay
 * @date 2020/9/7
 */
public abstract class AbstractMessageCodec<TDynamic extends BaseDynamicMessage, TInvoke extends BaseInvokeMessage<?>, TResult extends BaseResultMessage<?>> implements MessageCodec {
    private final MessageSerializer serializer;
    private final int gzipLength = 200;

    public AbstractMessageCodec(MessageSerializer serializer) {
        this.serializer = serializer;
    }

    protected byte[] encodeMessage(Object message) {
        if (message == null) {
            return new byte[0];
        }
        if (message instanceof byte[]) {
            return (byte[]) message;
        }
        if (message instanceof InvokeMessageImpl) {
            TInvoke model = CommonUtils.createGenericInstance(AbstractMessageCodec.class, 1);
            if (model != null) {
                model.setValue((InvokeMessageImpl) message);
                return this.serializer.serialize(model);
            }
        }
        if (message instanceof ResultMessageImpl) {
            TResult model = CommonUtils.createGenericInstance(AbstractMessageCodec.class, 2);
            if (model != null) {
                model.setResult((ResultMessageImpl) message);
                return this.serializer.serialize(model);
            }
        }
        return serializer.serializeNoType(message);

    }

    protected Object decodeMessage(byte[] data, Class<?> type) {
        if (CommonUtils.isEmpty(data)) {
            return null;
        }
        if (type.isAssignableFrom(InvokeMessage.class)) {
            Class<TInvoke> clazz = CommonUtils.getGenericClass(AbstractMessageCodec.class, 1);
            TInvoke model = this.serializer.deserializeT(data, clazz);
            if (model != null) {
                return model.getValue();
            }
        }

        if (type.isAssignableFrom(ResultMessage.class)) {
            Class<TResult> clazz = CommonUtils.getGenericClass(AbstractMessageCodec.class, 2);
            TResult model = this.serializer.deserializeT(data, clazz);
            if (model != null) {
                return model.getResult();
            }
        }
        return this.serializer.deserializeNoType(data, type);
    }

    @Override
    public byte[] encode(Object message, boolean gzip) {
        if (message == null) {
            return new byte[0];
        }
        byte[] buffer = encodeMessage(message);
        if (gzip && buffer.length > gzipLength) {
            return StreamUtils.gzip(buffer);
        }
        return buffer;
    }

    @Override
    public Object decode(byte[] data, Class<?> clazz, boolean gzip) {
        byte[] buffer = data;
        if (gzip) {
            buffer = StreamUtils.unGzip(buffer);
        }
        return decodeMessage(buffer, clazz);
    }
}
