package cn.spear.core.message.model.impl;

import cn.spear.core.message.model.DynamicMessage;
import cn.spear.core.message.model.InvokeMessage;
import cn.spear.core.util.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shay
 * @date 2020/9/7
 */
@Getter
@Setter
public class BaseInvokeMessage<T extends DynamicMessage> extends BaseMessage implements InvokeMessage<T> {
    private String serviceId;
    private Map<String, T> parameters;
    private Map<String, String> headers;

    public BaseInvokeMessage() {
        parameters = new HashMap<>();
        headers = new HashMap<>();
    }

    public BaseInvokeMessage(InvokeMessageImpl message) {
        this();
        this.initMessage(message);
    }


    public void initMessage(InvokeMessageImpl message) {
        this.setId(message.getId());
        this.serviceId = message.getServiceId();
        if (CommonUtils.isNotEmpty(message.getParameters())) {
            for (String key : message.getParameters().keySet()) {
                T item = createInstance();
                if (item == null) {
                    continue;
                }
                item.initValue(message.getParameters().get(key));
                this.parameters.put(key, item);
            }
        }
        if (CommonUtils.isNotEmpty(message.getHeaders())) {
            for (String key : message.getHeaders().keySet()) {
                this.headers.put(key, message.getHeaders().get(key));
            }
        }
    }

    public InvokeMessageImpl message() {
        InvokeMessageImpl message = new InvokeMessageImpl();
        message.setId(this.getId());
        message.setServiceId(this.serviceId);
        if (CommonUtils.isNotEmpty(this.parameters)) {
            Map<String, Object> parameters = new HashMap<>(this.parameters.size());
            for (String key : this.parameters.keySet()) {
                parameters.put(key, this.parameters.get(key).value());
            }
            message.setParameters(parameters);
        }
        if (CommonUtils.isNotEmpty(this.headers)) {
            Map<String, String> headers = new HashMap<>(this.headers.size());
            for (String key : this.headers.keySet()) {
                headers.put(key, this.headers.get(key));
            }
            message.setHeaders(headers);
        }
        return message;
    }
}
