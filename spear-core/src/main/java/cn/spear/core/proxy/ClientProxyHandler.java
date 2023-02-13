package cn.spear.core.proxy;

import cn.spear.core.exception.BusiException;
import cn.spear.core.ioc.IocContext;
import cn.spear.core.message.model.impl.DefaultInvokeMessage;
import cn.spear.core.message.model.impl.DefaultResultMessage;
import cn.spear.core.service.*;
import cn.spear.core.util.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author shay
 * @date 2020/9/15
 */
public class ClientProxyHandler implements InvocationHandler {
    private final ServiceFinder finder;
    private final ServiceClientFactory clientFactory;
    private final ServiceGenerator serviceGenerator;
    private final long timeout;

    public ClientProxyHandler(long timeout) {
        this.clientFactory = IocContext.getServiceT(ServiceClientFactory.class);
        this.finder = IocContext.getServiceT(ServiceFinder.class);
        this.serviceGenerator = IocContext.getServiceT(ServiceGenerator.class);
        this.timeout = timeout;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<ServiceAddress> addressList = this.finder.find(method.getDeclaringClass());
        ServiceAddress address = ArrayUtils.randomWeight(addressList);
        if (null == address) {
            throw new BusiException("服务未找到", 404);
        }
        ServiceClient client = clientFactory.create(address);
        DefaultInvokeMessage message = this.serviceGenerator.createMessage(method, args);
        DefaultResultMessage result = client.send(message, timeout);
        if (null == result || !result.success()) {
            String error = null == result ? "服务调用异常" : result.getMessage();
            int code = null == result ? 500 : result.getCode();
            throw new BusiException(error, code);
        }
        return result.getContent();
    }
}
