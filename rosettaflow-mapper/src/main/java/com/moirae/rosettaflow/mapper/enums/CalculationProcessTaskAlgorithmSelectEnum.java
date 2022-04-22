package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CalculationProcessTaskAlgorithmSelectEnum {

    USER_ALG(0, "用户输入母算法"),
    USER_TRAIN_ALG(1, "用户输入子训练算法"),
    USER_PREDICT_ALG(2, "用户输入子预测算法"),
    BUILD_IN_ALG(3, "内置PSI算法");

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    CalculationProcessTaskAlgorithmSelectEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
