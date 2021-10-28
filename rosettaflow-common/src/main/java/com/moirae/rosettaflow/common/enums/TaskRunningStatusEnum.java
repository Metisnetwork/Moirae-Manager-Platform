package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 任务运行状态描述
 */
public enum TaskRunningStatusEnum {
    /**
     * 未知
     */
    UN_VALID(0),
    /**
     * 等在中
     */
    WAITING((byte) 1),
    /**
     * 计算中
     */
    CALCULATING((byte) 2),
    /**
     * 计算中
     */
    FAIL((byte) 3),
    /**
     * 成功
     */
    SUCCESS((byte) 4);

    private final int value;

    TaskRunningStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
