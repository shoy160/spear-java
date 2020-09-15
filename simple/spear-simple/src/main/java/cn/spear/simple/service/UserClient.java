package cn.spear.simple.service;

import cn.spear.core.service.annotation.SpearService;
import cn.spear.simple.dto.UserDTO;
import cn.spear.simple.dto.UserSearchDTO;

import java.util.List;

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

    /**
     * 搜索
     *
     * @param dto dto
     * @return list
     */
    @SpearService(route = "search")
    List<UserDTO> search(UserSearchDTO dto);
}
