package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/10/30
 * @description 是否需要输入模型
 */
public enum InputModelEnum {

    /**
     * 否
     */
    UN_NEED(0),
    /**
     * 是
     */
    NEED(1);

    private final int value;

    InputModelEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
