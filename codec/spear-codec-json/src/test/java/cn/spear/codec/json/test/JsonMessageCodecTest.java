package cn.spear.codec.json.test;

import cn.spear.codec.json.JsonMessageCodec;
import cn.spear.codec.json.JsonMessageSerializer;
import cn.spear.codec.json.test.model.UserDTO;
import cn.spear.codec.json.test.model.UserSearchDTO;
import cn.spear.core.message.MessageCodec;
import cn.spear.core.message.MessageSerializer;
import cn.spear.core.message.model.impl.InvokeMessageImpl;
import cn.spear.core.message.model.impl.ResultMessageImpl;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.RandomUtils;
import cn.spear.core.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shay
 * @date 2020/9/8
 */
@Slf4j
public class JsonMessageCodecTest {

    private final MessageCodec codec;

    public JsonMessageCodecTest() {
        MessageSerializer serializer = new JsonMessageSerializer();
        this.codec = new JsonMessageCodec(serializer);
    }

    @Test
    public void encodeTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "shay");
        map.put("age", 20);
        byte[] buffer = codec.encode(map);
        log.info("encode:{}", new String(buffer));
        Map<?, ?> result = codec.decodeT(buffer, map.getClass());
        log.info(result.getClass().getTypeName());
        Assert.assertEquals(result.size(), 2);
    }

    @Test
    public void invokeMessageTest() {
        InvokeMessageImpl message = new InvokeMessageImpl(RandomUtils.fastId());
        message.setServiceId("testService");
        UserSearchDTO searchDTO = new UserSearchDTO();
        searchDTO.setId(1001);
        searchDTO.setKeyword("shay");
        message.addParameter("dto", searchDTO);

        message.addHeader("ip", "127.0.0.1");
        message.addHeader("host", "localhost");
        byte[] buffer = codec.encode(message);
        log.info("encode:{}", new String(StreamUtils.unGzip(buffer)));
        message = codec.decodeT(buffer, InvokeMessageImpl.class);
        Assert.assertEquals(message.getServiceId(), "testService");
    }

    @Test
    public void resultMessageTest() {
        ResultMessageImpl result = new ResultMessageImpl(RandomUtils.fastId());
        result.setCode(200);
        UserDTO dto = new UserDTO();
        dto.setId(1001);
        dto.setName(RandomUtils.randomString(100));
        dto.setRole("system");
        dto.setEmail("132456@qq.com");
        result.setContent(dto);
        byte[] buffer = codec.encode(result);
        log.info("encode:{}", new String(StreamUtils.unGzip(buffer)));
        ResultMessageImpl result1 = codec.decodeT(buffer, ResultMessageImpl.class);
        Assert.assertEquals(200, (int) result1.getCode());
        UserDTO content = CommonUtils.cast(result.getContent(), UserDTO.class);
        Assert.assertEquals(1001, (int) content.getId());
    }
}