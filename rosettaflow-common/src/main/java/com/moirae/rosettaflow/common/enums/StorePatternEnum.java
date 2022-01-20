package com.moirae.rosettaflow.common.enums;

/**
 * 算法输出存储形式
 * @author hudenian
 * @date 2021/9/26
 */
public enum StorePatternEnum {
    /**
     * 明文
     */
    FALSE((byte) 1),
    /**
     * 密文
     */
    TRUE((byte) 2);

    private final byte value;

    StorePatternEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
