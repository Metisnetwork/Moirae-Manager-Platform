package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/12/16
 */
public enum ValidFlagEnum {
    /**
     * 无效
     */
    UN_VALID((byte) 0),
    /**
     * 有效
     */
    VALID((byte) 1);

    private final byte value;

    ValidFlagEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
