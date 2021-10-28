package com.moirae.rosettaflow.common.enums;

/**
 * @author juzix
 * @date 2021/8/13
 * @description 作业操作类型
 */
public enum JobActionStatusEnum {
    /**
     * 暂停
     */
    PAUSE((byte) 1),
    /**
     * 重启
     */
    RESTART((byte) 2);

    private final byte value;

    JobActionStatusEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
