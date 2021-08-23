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
    UNKNOWN((byte) 0),
    /**
     * 以太坊地址
     */
    ETH((byte) 1),
    /**
     * Alaya地址
     */
    ATP((byte) 2),
    /**
     * PlatON地址
     */
    LAT((byte) 3);

    private final byte value;

    UserTypeEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
