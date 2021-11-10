package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 授权类型
 */
public enum AuthTypeEnum {

    /**
     * 未知
     */
    UNKNOWN((byte) 0),
    /**
     * 按时间
     */
    TIME((byte) 1),
    /**
     * 按次数
     */
    NUMBER((byte) 2);

    private final byte value;

    AuthTypeEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
