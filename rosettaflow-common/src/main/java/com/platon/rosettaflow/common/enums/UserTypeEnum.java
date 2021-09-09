package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 用户类型枚举
 */
public enum UserTypeEnum {
    /**
     * 未定义
     */
    UNKNOWN(0),
    /**
     * 以太坊地址
     */
    ETH(1),
    /**
     * Alaya地址
     */
    ALAYA(2),
    /**
     * PlatON地址
     */
    PLATON(3);

    private final int value;

    UserTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
