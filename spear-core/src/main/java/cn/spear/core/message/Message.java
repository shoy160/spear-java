package cn.spear.core.message;

import java.io.Serializable;

/**
 * 消息实体
 *
 * @author shay
 * @date 2020/9/4
 */
public interface Message extends Serializable {
    /**
     * 获取ID
     *
     * @return Id
     */
    String getId();
}
