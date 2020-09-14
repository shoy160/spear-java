package cn.spear.simple.service.impl;

import cn.spear.simple.service.UserClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shay
 * @date 2020/9/14
 */
@Slf4j
public class UserClientImpl implements UserClient {
    @Override
    public String hello(String name) {
        return String.format("Hello %s!", name);
    }

    @Override
    public void add(String name) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("add name:{}", name);
    }

    @Override
    public void add(int age) {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("add age:{}", age);
    }
}
