package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description job处理状态
 */
public enum JobStatusEnum {
    /**
     * 未结束
     */
    UNFINISH((byte) 0),
    /**
     * 已结束
     */
    FINISH((byte) 1);

    private final byte value;

    JobStatusEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
