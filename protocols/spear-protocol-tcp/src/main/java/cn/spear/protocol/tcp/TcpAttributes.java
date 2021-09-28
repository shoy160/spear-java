package cn.spear.protocol.tcp;

import cn.spear.core.message.MessageListener;
import cn.spear.core.message.MessageSender;
import cn.spear.core.service.ServiceAddress;
import io.netty.util.AttributeKey;

/**
 * @author shoy
 * @date 2021/9/28
 */
public interface TcpAttributes {
    AttributeKey<ServiceAddress> ADDRESS_KEY = AttributeKey.valueOf("address");
    AttributeKey<MessageSender> SENDER_KEY = AttributeKey.valueOf("sender");
    AttributeKey<MessageListener> LISTENER_KEY = AttributeKey.valueOf("listener");
}
