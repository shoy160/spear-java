package cn.spear.core.service;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.function.Function;

/**
 * @author shay
 * @date 2020/9/4
 */
@Getter
@Setter
public class ServiceEntry {
    private final Method method;
    private final Parameter[] parameters;
    private final boolean notify;
    private Function<Map<String, Object>, Object> invoke;

    public ServiceEntry(Method method) {
        this.method = method;
        this.notify = method.getReturnType() == void.class;
        this.parameters = method.getParameters();
    }

}
