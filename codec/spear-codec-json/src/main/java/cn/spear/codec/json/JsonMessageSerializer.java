package cn.spear.codec.json;

import cn.spear.core.message.MessageSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Json 消息序列化
 *
 * @author shay
 * @date 2020/9/7
 */
public class JsonMessageSerializer implements MessageSerializer {

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Override
    public byte[] serialize(Object value) {
        try {
            ObjectMapper mapper = getMapper();
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] data, Class<?> type) {
        try {
            ObjectMapper mapper = getMapper();
            return mapper.readValue(data, type);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T deserializeT(byte[] data, Class<T> type) {
        try {
            ObjectMapper mapper = getMapper();
            return mapper.readValue(data, type);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
