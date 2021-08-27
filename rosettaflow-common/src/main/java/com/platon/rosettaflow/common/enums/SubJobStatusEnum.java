package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 子作业job处理状态
 */
public enum SubJobStatusEnum {
    /**
     * 未开始
     */
    UN_RUN((byte) 0),
    /**
     * 运行中
     */
    RUNNING((byte) 1),
    /**
     * 运行成功
     */
    RUN_SUCCESS((byte) 2),
    /**
     * 运行失败
     */
    RUN_FAIL((byte) 3);

    private final byte value;

    SubJobStatusEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
