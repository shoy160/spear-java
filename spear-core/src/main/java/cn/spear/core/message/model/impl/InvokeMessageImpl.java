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
public class InvokeMessageImpl extends BaseMessage implements InvokeMessage<Object> {
    private String serviceId;
    private Map<String, Object> parameters;
    private Map<String, String> headers;

    public InvokeMessageImpl() {
        this(null);
    }

    public InvokeMessageImpl(String id) {
        super(id);
        parameters = new HashMap<>();
        headers = new HashMap<>();
    }
}
