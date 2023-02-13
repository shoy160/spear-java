package cn.spear.protocol.tcp;

import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.service.ServiceAddress;
import io.netty.util.AttributeKey;

/**
 * todo
 *
 * @author shay
 * @date 2023/2/13
 **/
public final class ChannelAttributes {
    public static final AttributeKey<ServiceAddress> ADDRESS_KEY = AttributeKey.valueOf("address");
    public static final AttributeKey<MessageSender> SENDER_KEY = AttributeKey.valueOf("sender");
    public static final AttributeKey<MessageListener> LISTENER_KEY = AttributeKey.valueOf("listener");
}
