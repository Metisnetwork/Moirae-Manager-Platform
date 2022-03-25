package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CalculationProcessStepEnum {

    SELECT_TRAINING_INPUT(0, "选择训练输入数据"),
    SELECT_PREDICTION_INPUT(1, "选择预测输入数据"),
    SELECT_PSI_INPUT(2, "选择PSI输入数据"),
    SELECT_RESOURCE(3, "选择计算环境"),
    SELECT_OUTPUT(4, "选择结果接收方");

    @JsonValue
    private Integer value;
    private String desc;

    CalculationProcessStepEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }
}
