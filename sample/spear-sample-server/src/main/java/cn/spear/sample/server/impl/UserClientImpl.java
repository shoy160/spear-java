package cn.spear.sample.server.impl;

import cn.spear.sample.contract.UserClient;
import cn.spear.sample.contract.dto.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static List<UserDTO> datasource = new ArrayList<>(Arrays.asList(new UserDTO[]{
            UserDTO.builder().id(1).name("name01").role(0).gender(UserGender.Female).build(),
            UserDTO.builder().id(2).name("name02").role(2).gender(UserGender.Male).build(),
            UserDTO.builder().id(3).name("name03").role(2).gender(UserGender.Female).build(),
            UserDTO.builder().id(4).name("name04").role(0).gender(UserGender.Male).build()
    }));

    @Override
    public List<UserDTO> search(UserSearchDTO dto) {
        return datasource;
    }
}
