package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 是否已过期（按时间时需要）
 */
public enum ExpireTypeEnum {
    /**
     * 未过期
     */
    UN_EXPIRE((byte) 0),
    /**
     * 已过期
     */
    EXPIRE((byte) 1);

    private final byte value;

    ExpireTypeEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
