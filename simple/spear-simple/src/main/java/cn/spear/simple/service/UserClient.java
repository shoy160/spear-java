package cn.spear.simple.service;

import cn.spear.core.service.annotation.SpearService;

/**
 * @author shay
 * @date 2020/9/14
 */
@SpearService(route = "spear/user")
public interface UserClient {
    /**
     * Hello
     *
     * @param name name
     * @return String
     */
    @SpearService(route = "hello")
    String hello(String name);

    /**
     * add
     *
     * @param name name
     */
    void add(String name);

    /**
     * add
     *
     * @param age age
     */
    void add(int age);
}
