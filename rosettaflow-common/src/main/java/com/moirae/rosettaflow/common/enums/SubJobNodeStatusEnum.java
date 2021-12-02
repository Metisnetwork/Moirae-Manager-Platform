package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 子作业节点运行状态
 */
public enum SubJobNodeStatusEnum {
    /**
     * 未开始
     */
    UN_RUN((byte) 0, "not started"),
    /**
     * 运行中
     */
    RUNNING((byte) 1, "running"),
    /**
     * 运行成功
     */
    RUN_SUCCESS((byte) 2, "run success"),
    /**
     * 运行失败
     */
    RUN_FAIL((byte) 3, "run fail");

    private final byte value;

    private final String msg;

    SubJobNodeStatusEnum(byte value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public byte getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }


}
