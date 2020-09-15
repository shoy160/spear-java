package cn.spear.core.service.impl;

import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.service.ServiceGenerator;
import cn.spear.core.service.annotation.SpearService;
import cn.spear.core.session.SpearClaimTypes;
import cn.spear.core.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shay
 * @date 2020/9/15
 */
@Slf4j
public class DefaultServiceGenerator implements ServiceGenerator {
    private final Map<Method, String> routeCache;

    public DefaultServiceGenerator() {
        this.routeCache = new HashMap<>();
    }

    @Override
    public String getServiceId(Method method) {
        if (routeCache.containsKey(method)) {
            return routeCache.get(method);
        }
        Class<?> clazz = method.getDeclaringClass();
        SpearService clazzService = clazz.getAnnotation(SpearService.class);
        String prefix = null == clazzService ? clazz.getName() : clazzService.route();
        SpearService service = method.getAnnotation(SpearService.class);
        String methodRoute;
        if (null != service && CommonUtils.isNotEmpty(service.route())) {
            methodRoute = service.route();
        } else {
            StringBuilder nameBuilder = new StringBuilder(method.getName());
            Parameter[] parameters = method.getParameters();
            if (CommonUtils.isNotEmpty(parameters)) {
                for (Parameter parameter : parameters) {
                    nameBuilder.append("_").append(parameter.getType().getSimpleName());
                }
            }
            methodRoute = nameBuilder.toString().toLowerCase();
        }
        String route = String.format("%s/%s", prefix, methodRoute);
        if (log.isInfoEnabled()) {
            log.info("{}_{},生成服务条目ID:{}", method.getDeclaringClass().getName(), method.getName(), route);
        }
        routeCache.put(method, route);
        return route;
    }

    @Override
    public DefaultInvokeMessage createMessage(Method method, Object[] args) {
        DefaultInvokeMessage message = new DefaultInvokeMessage();
        String serviceId = this.getServiceId(method);
        message.setServiceId(serviceId);
        message.setNotify(method.getReturnType().equals(void.class));
        Parameter[] parameters = method.getParameters();
        if (CommonUtils.isNotEmpty(parameters)) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < parameters.length; i++) {
                map.put(parameters[i].getName(), args[i]);
            }
            message.setParameters(map);
        }
        //header
        Map<String, String> headers = new HashMap<>(1);
        headers.put(SpearClaimTypes.HEADER_USER_AGENT, "spear-client");
        message.setHeaders(headers);
        return message;
    }
}
