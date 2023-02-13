package cn.spear.core.proxy.http;

import cn.spear.core.Constants;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.TypeUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author shay
 * @date 2020/8/14
 */
@Getter
@Setter
@Slf4j
public class HttpResponse {
    private Integer code;
    private String charset;
    private String contentType;
    private byte[] body;

    public HttpResponse() {
        this.code = 404;
        this.charset = "UTF-8";
    }

    public InputStream readStream() {
        if (body == null) {
            return null;
        }
        return new ByteArrayInputStream(this.body);
    }

    public void setContentType(String contentType) {
        if (CommonUtils.isEmpty(contentType)) {
            return;
        }
        this.contentType = contentType.split(";")[0];
    }

    public boolean isSuccessCode() {
        return this.code == 200;
    }

    public boolean isErrorCode() {
        return this.code != 200;
    }

    public String readBody() {
        return readBody(Constants.EMPTY_STR);
    }

    public String readBody(String charset) {
        if (body == null) {
            return null;
        }
        try {
            if (CommonUtils.isEmpty(charset)) {
                charset = this.charset;
            }
            return new String(this.body, charset);
        } catch (IOException ex) {
            log.warn("读取Http响应内容失败", ex);
            return null;
        }
    }

    public <T> T readBodyT(Class<T> clazz, T defaultValue) {
        T value = readBodyT(clazz);
        return value == null ? defaultValue : value;
    }

    public <T> T readBodyT(Class<T> clazz) {
        try {
            String body = readBody();
            if (TypeUtils.isSimple(clazz)) {
                return clazz.cast(body);
            }
            switch (this.contentType) {
                case Constants.CONTENT_TYPE_JSON:
                    //todo
                    return null;
//                    return JsonUtils.json(body, clazz);
                case Constants.CONTENT_TYPE_XML:
                    //todo
                    return null;
//                    return XmlUtils.deserialize(body, clazz);
                default:
                    return null;
            }
        } catch (Exception ex) {
            log.warn("读取Http响应内容失败", ex);
            return null;
        }
    }

    public Object readBody(Type type) {
        if (type instanceof Class) {
            return readBodyT((Class<?>) type);
        }
        try {
            String body = readBody();
            switch (this.contentType) {
                case Constants.CONTENT_TYPE_JSON:
                    //todo
//                    return JsonUtils.json(body, factory -> factory.constructType(type));
                case Constants.CONTENT_TYPE_XML:
                    //todo
//                    return XmlUtils.deserialize(body, type.getClass());
                default:
                    return null;
            }
        } catch (Exception ex) {
            log.warn("读取Http响应内容失败", ex);
            return null;
        }
    }
}
