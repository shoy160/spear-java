package cn.spear.simple.contract.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shay
 * @date 2020/9/15
 */
@Getter
@Setter
public class UserSearchDTO {
    private String name;
    private UserGender gender;
}
