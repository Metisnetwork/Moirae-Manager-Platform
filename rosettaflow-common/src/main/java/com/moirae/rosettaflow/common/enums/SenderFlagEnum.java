package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/9/26
 * @description 是否是发起方状态
 */
public enum SenderFlagEnum {
    /**
     * 不是发起方
     */
    FALSE((byte) 0),
    /**
     * 发起方
     */
    TRUE((byte) 1);

    private final byte value;

    SenderFlagEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
