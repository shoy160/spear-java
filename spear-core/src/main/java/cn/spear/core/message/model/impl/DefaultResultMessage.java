package cn.spear.core.message.model.impl;

import cn.spear.core.message.model.ResultMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shay
 * @date 2020/9/4
 */
@Getter
@Setter
public class DefaultResultMessage extends BaseMessage implements ResultMessage<Object> {
    private Integer code;
    private String message;
    private Object content;

    public boolean success() {
        return this.code == 200;
    }

    public DefaultResultMessage() {
        this(null);
        this.code = 200;
    }

    public DefaultResultMessage(String id) {
        super(id);
    }

    public DefaultResultMessage(String message, int code) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "DefaultResultMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }
}
