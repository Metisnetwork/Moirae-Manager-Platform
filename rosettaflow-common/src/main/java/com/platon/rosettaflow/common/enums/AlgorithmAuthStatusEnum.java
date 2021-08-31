package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/31
 * @description 算法授权状态枚举
 */
public enum AlgorithmAuthStatusEnum {
    /**
     * 0-待申请
     */
    UN_AUTH((byte) 0),
    /**
     * 1-申请中
     */
    AUTHING((byte) 1),
    /**
     * 2-已授权
     */
    AUTH((byte) 2),
    /**
     * 3-已拒绝
     */
    REFUSE((byte) 3);

    private final byte value;

    AlgorithmAuthStatusEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
