package cn.spear.core.proxy.http.annotation;

import java.lang.annotation.*;

/**
 * @author shay
 * @date 2021/3/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Json {
}
