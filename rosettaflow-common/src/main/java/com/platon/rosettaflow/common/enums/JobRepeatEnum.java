package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description job是否重复执行
 */
public enum JobRepeatEnum {
    /**
     * 不重复执行
     */
    NOREPEAT((byte) 0),
    /**
     * 重复执行
     */
    REPEAT((byte) 1);

    private final byte value;

    JobRepeatEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
