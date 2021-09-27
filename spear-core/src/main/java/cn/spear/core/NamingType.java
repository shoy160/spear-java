package cn.spear.core;

import cn.spear.core.lang.enums.BaseEnum;

/**
 * 命名方式
 *
 * @author shay
 * @date 2020/11/11
 */
public enum NamingType implements BaseEnum {
    /**
     * Normal
     */
    Normal(0),
    CamelCase(1),
    UrlCase(2),
    UpperCase(3);

    private final int value;

    private NamingType(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
