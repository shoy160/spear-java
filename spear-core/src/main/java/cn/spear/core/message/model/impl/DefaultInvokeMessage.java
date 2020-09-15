package cn.spear.core.message.model.impl;

import cn.spear.core.message.model.InvokeMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shay
 * @date 2020/9/4
 */
@Getter
@Setter
public class DefaultInvokeMessage extends BaseMessage implements InvokeMessage<Object> {
    private String serviceId;
    private Boolean notify;
    private Map<String, Object> parameters;
    private Map<String, String> headers;

    public DefaultInvokeMessage() {
        this(null);
    }

    public DefaultInvokeMessage(String id) {
        super(id);
        parameters = new HashMap<>();
        headers = new HashMap<>();
    }

    @Override
    public String toString() {
        return "{" +
                "serviceId='" + this.serviceId + '\'' +
                ", notify='" + this.notify + '\'' +
                ", parameters=" + this.parameters +
                ", headers=" + this.headers +
                '}';
    }
}
