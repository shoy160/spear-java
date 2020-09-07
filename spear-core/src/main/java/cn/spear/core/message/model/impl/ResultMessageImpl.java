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
public class ResultMessageImpl extends BaseMessage implements ResultMessage<Object> {
    private Integer code;
    private String message;
    private Object content;

    public ResultMessageImpl() {
    }

    public ResultMessageImpl(String message, int code) {
        this.code = code;
        this.message = message;
    }
}
