package cn.spear.core.message.model.impl;

import cn.spear.core.message.model.Message;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.IdentityUtils;
import cn.spear.core.util.RandomUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础消息实体
 *
 * @author shay
 * @date 2020/9/4
 */
@Getter
@Setter
public class BaseMessage implements Message {
    private String id;

    public BaseMessage() {
        this(null);
    }

    public BaseMessage(String id) {
        if (CommonUtils.isEmpty(id)) {
            this.id = IdentityUtils.fastId();
        } else {
            this.id = id;
        }
    }
}
