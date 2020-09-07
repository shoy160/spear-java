package cn.spear.core.test;

import cn.spear.core.message.model.impl.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 消息测试
 *
 * @author shay
 * @date 2020/9/4
 */
@Slf4j
public class MessageTest {

    @Test
    public void BaseTest() {
        BaseMessage msg = new BaseMessage();
        log.info(msg.getId());
    }
}
