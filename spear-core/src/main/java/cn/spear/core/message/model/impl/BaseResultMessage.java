package cn.spear.core.message.model.impl;

import cn.spear.core.message.model.DynamicMessage;
import cn.spear.core.message.model.ResultMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shay
 * @date 2020/9/7
 */
@Getter
@Setter
public class BaseResultMessage<T extends DynamicMessage> extends BaseMessage implements ResultMessage<T> {
    private Integer code;
    private String message;
    private T content;

    public BaseResultMessage() {
    }

    public BaseResultMessage(ResultMessage<?> message) {

    }

    public void setResult(ResultMessageImpl result) {
        this.setId(result.getId());
        this.code = result.getCode();
        this.message = result.getMessage();
        if (result.getContent() != null) {
            this.content = this.createInstance();
            this.content.setValue(result.getContent());
        }
    }

    public ResultMessageImpl getResult() {
        ResultMessageImpl result = new ResultMessageImpl();
        result.setId(this.getId());
        result.setCode(this.code);
        result.setMessage(this.message);
        if (this.content != null) {
            result.setContent(this.content.getValue());
        }
        return result;
    }
}
