package cn.spear.core.proxy.http.annotation;

import cn.spear.core.proxy.http.enums.HttpMethod;

import java.lang.annotation.*;

/**
 * 路由配置
 *
 * @author shay
 * @date 2021/3/18
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Route {
    String value();

    HttpMethod method() default HttpMethod.GET;
}
