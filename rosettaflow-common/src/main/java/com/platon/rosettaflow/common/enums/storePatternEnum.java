package com.platon.rosettaflow.common.enums;

/**
 * 算法输出存储形式
 * @author hudenian
 * @date 2021/9/26
 */
public enum storePatternEnum {
    /**
     * 明文
     */
    FALSE((byte) 1),
    /**
     * 密文
     */
    TRUE((byte) 2);

    private final byte value;

    storePatternEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
