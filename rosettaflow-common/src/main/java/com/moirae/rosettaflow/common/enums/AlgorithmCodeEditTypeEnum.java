package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 功能描述
 */
public enum AlgorithmCodeEditTypeEnum {
    /**
     * sql
     */
    SQL((byte) 1),
    /**
     * noteBook
     */
    NOTEBOOK((byte) 2);

    private final byte value;

    AlgorithmCodeEditTypeEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
