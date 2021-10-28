package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 元数据的使用方式定义
 */
public enum MetaDataUsageEnum {
    /**
     * 未定义类型
     */
    @SuppressWarnings("unused")
    USAGE_UNKNOWN(0),
    /**
     * 依照时间段来使用
     */
    PERIOD(1),
    /**
     * 依照次数来使用
     */
    TIMES(2);

    private final int value;

    MetaDataUsageEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
