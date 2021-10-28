package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 功能描述
 */
public enum StatusEnum {
    /**
     * 无效
     */
    UN_VALID((byte) 0),
    /**
     * 有效
     */
    VALID((byte) 1);

    private final byte value;

    StatusEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
