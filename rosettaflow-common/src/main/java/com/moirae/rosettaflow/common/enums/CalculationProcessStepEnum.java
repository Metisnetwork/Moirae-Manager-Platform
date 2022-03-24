package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CalculationProcessStepEnum {

    SELECT_TRAINING_INPUT("selectTrainingInput", "选择训练输入数据"),
    SELECT_PREDICTION_INPUT("selectPredictionInput", "选择预测输入数据"),
    SELECT_PSI_INPUT("selectPsiInput", "选择PSI输入数据"),
    SELECT_RESOURCE("selectResource", "选择计算环境"),
    SELECT_OUTPUT("selectOutput", "选择结果接收方");

    @JsonValue
    private String value;
    private String desc;

    CalculationProcessStepEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
