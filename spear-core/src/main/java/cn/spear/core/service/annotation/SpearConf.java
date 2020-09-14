package cn.spear.core.service.annotation;

import cn.spear.core.service.enums.ServiceCodec;
import cn.spear.core.service.enums.ServiceProtocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shay
 * @date 2020/9/14
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpearConf {
    ServiceProtocol protocol() default ServiceProtocol.Tcp;

    ServiceCodec codec() default ServiceCodec.Json;
}
