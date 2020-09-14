package cn.spear.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author shay
 * @date 2020/9/14
 */
public interface ServiceInvoke {
    /**
     * 执行方法
     *
     * @param params 参数
     * @return result
     * @throws InvocationTargetException ex
     * @throws IllegalAccessException    ex
     */
    Object invoke(Map<String, Object> params) throws InvocationTargetException, IllegalAccessException;
}
