package cn.spear.core.proxy.http.handler;

import cn.spear.core.Constants;
import cn.spear.core.proxy.http.HttpClientFilter;
import cn.spear.core.proxy.http.HttpHelper;
import cn.spear.core.proxy.http.HttpRequest;
import cn.spear.core.proxy.http.HttpResponse;
import cn.spear.core.proxy.http.annotation.*;
import cn.spear.core.proxy.http.enums.HttpContentType;
import cn.spear.core.proxy.http.enums.HttpMethod;
import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.MapUtils;
import cn.spear.core.util.TypeUtils;
import cn.spear.core.util.UrlUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shay
 * @date 2021/3/18
 */
@Slf4j
public class HttpProxyHandler implements InvocationHandler {

    private final Integer timeout;
    private final HttpClientFilter filter;

    public HttpProxyHandler(Integer timeout, HttpClientFilter filter) {
        this.timeout = timeout;
        this.filter = filter;
    }

    @Getter
    @Setter
    @Builder
    private static class HttpRoute {
        String url;
        HttpMethod method;
    }

    private HttpRoute getRoute(Method method) {
        Route route = method.getAnnotation(Route.class);
        if (route != null) {
            return HttpRoute.builder().url(route.value()).method(route.method()).build();
        }
        Get get = method.getAnnotation(Get.class);
        if (get != null) {
            return HttpRoute.builder().url(get.value()).method(HttpMethod.GET).build();
        }
        Post post = method.getAnnotation(Post.class);
        if (post != null) {
            return HttpRoute.builder().url(post.value()).method(HttpMethod.POST).build();
        }
        Put put = method.getAnnotation(Put.class);
        if (put != null) {
            return HttpRoute.builder().url(put.value()).method(HttpMethod.PUT).build();
        }
        Delete delete = method.getAnnotation(Delete.class);
        if (delete != null) {
            return HttpRoute.builder().url(delete.value()).method(HttpMethod.DELETE).build();
        }
        Patch patch = method.getAnnotation(Patch.class);
        if (patch != null) {
            return HttpRoute.builder().url(patch.value()).method(HttpMethod.PATCH).build();
        }
        Option option = method.getAnnotation(Option.class);
        if (option != null) {
            return HttpRoute.builder().url(option.value()).method(HttpMethod.OPTION).build();
        }
        return null;
    }

    private void setRequestParameter(HttpRequest request, Parameter parameter, Object value, Map<String, Object> queryMap) {
        Json content = parameter.getAnnotation(Json.class);
        if (content != null) {
            request.setData(value);
            request.setContentType(HttpContentType.Json);
            return;
        }
        Xml xml = parameter.getAnnotation(Xml.class);
        if (xml != null) {
            request.setData(value);
            request.setContentType(HttpContentType.Xml);
            return;
        }
        Form form = parameter.getAnnotation(Form.class);
        if (form != null) {
            request.setData(value);
            request.setContentType(HttpContentType.Form);
            return;
        }
        String name = parameter.getName();
        String regex = String.format("\\{%s\\}", name);
        Pattern compile = Pattern.compile(regex, Pattern.DOTALL);
        String url = request.getUrl();
        Matcher matcher = compile.matcher(url);
        if (matcher.find()) {
            url = url.replaceAll(regex, value.toString());
            request.setUrl(url);
        } else {
            if (TypeUtils.isSimple(parameter.getType())) {
                queryMap.put(name, value);
            } else {
                Map<String, Object> map = MapUtils.map(value);
                for (String key : map.keySet()) {
                    queryMap.put(key, map.get(key));
                }
            }
        }
    }

    private HttpRequest createRequest(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        String url = "";
        HttpMethod httpMethod = HttpMethod.GET;
        Map<String, String> headerMap = new HashMap<>();
        //interface 注解
        Host host = clazz.getAnnotation(Host.class);
        if (host != null) {
            url = host.value();
        }
        Route route = clazz.getAnnotation(Route.class);
        if (route != null) {
            url = UrlUtil.completeUrl(url, route.value());
            httpMethod = route.method();
        }
        Header[] headers = clazz.getAnnotationsByType(Header.class);
        if (CommonUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                headerMap.put(header.key(), header.value());
            }
        }
        //method 注解
        host = method.getAnnotation(Host.class);
        if (host != null) {
            url = host.value();
        }
        HttpRoute httpRoute = getRoute(method);
        if (httpRoute != null) {
            url = UrlUtil.completeUrl(url, httpRoute.getUrl());
            httpMethod = httpRoute.getMethod();
        }
        headers = method.getAnnotationsByType(Header.class);
        if (CommonUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                headerMap.put(header.key(), header.value());
            }
        }
        HttpRequest request = new HttpRequest(url, httpMethod);
        if (this.timeout > 0) {
            request.setReadTimeout(this.timeout);
        }
        request.setHeaders(headerMap);
        return request;
    }

    private void updateRequestData(HttpRequest request, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        if (CommonUtils.isEmpty(parameters)) {
            return;
        }
        Map<String, Object> queryMap = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = args[i];
            if (value == null) {
                continue;
            }
            setRequestParameter(request, parameter, value, queryMap);
        }
        //替换掉未匹配的参数
        String url = request.getUrl().replaceAll("\\{[^\\}]+\\}", Constants.EMPTY_STR);
        request.setUrl(url);
        request.setParams(queryMap);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        HttpRequest request = createRequest(method);
        updateRequestData(request, method, args);
        if (filter != null) {
            filter.before(request);
        }
        HttpResponse response = HttpHelper.request(request);
        if (filter != null) {
            filter.after(response, request);
        }

        if (response.isErrorCode()) {
            log.warn("request error: code {}", response.getCode());
            log.debug(response.readBody());
        }
        Type type = method.getGenericReturnType();
        return response.readBody(type);
    }
}
