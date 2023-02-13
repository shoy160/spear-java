package cn.spear.simple.server.impl;

import cn.spear.core.util.StringUtils;
import cn.spear.simple.contract.UserClient;
import cn.spear.simple.contract.dto.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author shay
 * @date 2020/9/14
 */
@Slf4j
public class UserClientImpl implements UserClient {
    private final List<UserDTO> datasource;

    public UserClientImpl() {
        final int size = 100;
        this.datasource = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int id = 1001 + i;
            UserDTO dto = UserDTO.builder()
                    .id(id)
                    .name(String.format("name%s", id))
                    .role(0)
                    .gender(UserGender.Female)
                    .build();
            this.datasource.add(dto);
        }
    }

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


    @Override
    public List<UserDTO> search(UserSearchDTO dto) {
        return datasource.stream().filter(t -> {
            Predicate<Boolean> condition = Predicate.isEqual(true);
            if (Objects.nonNull(dto.getName())) {
                condition = condition.and(d ->
                        Objects.nonNull(t.getName())
                                && t.getName().contains(dto.getName())
                );
            }
            if (Objects.nonNull(dto.getGender())) {
                condition = condition.and(d ->
                        Objects.nonNull(t.getGender())
                                && t.getGender().equals(dto.getGender())
                );
            }
            return condition.test(true);
        }).collect(Collectors.toList());
    }
}
