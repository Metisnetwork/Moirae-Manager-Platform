package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description job处理状态
 */
public enum JobStatusEnum {
    /**
     * 未开始
     */
    UN_START((byte) 0),
    /**
     * 运行中
     */
    RUNNING((byte) 1),
    /**
     * 已停止
     */
    STOP((byte) 2),
    /**
     * 已结束（超出运行结束时间，无法再重启）
     */
    FINISH((byte) 3);

    private final byte value;

    JobStatusEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
