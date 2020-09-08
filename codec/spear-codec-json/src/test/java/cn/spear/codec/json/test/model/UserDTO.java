package cn.spear.codec.json.test.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shay
 * @date 2020/9/8
 */
@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String name;
    private String role;
    private String email;
}
