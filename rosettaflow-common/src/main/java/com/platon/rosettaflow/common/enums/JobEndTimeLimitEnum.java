package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description job结束时间限制
 */
public enum JobEndTimeLimitEnum {
    /**
     * 不限制
     */
    NOLIMIT((byte) 0),
    /**
     * 限制
     */
    LIMIT((byte) 1);

    private final byte value;

    JobEndTimeLimitEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
