package cn.spear.sample.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shay
 * @date 2020/9/15
 */
@Getter
@Setter
@Builder
public class UserDTO {
    private Integer id;
    private String name;
    private Integer role;
    private UserGender gender;
}
