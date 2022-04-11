package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum CalculationProcessTypeEnum {

    INPUT_TRAINING(0, "选择训练输入数据"),
    INPUT_PREDICTION(1, "选择预测输入数据"),
    INPUT_PSI(2, "选择PSI输入数据"),
    RESOURCE_COMMON(3, "选择计算环境(通用)"),
    RESOURCE_TRAINING_PREDICTION(4, "选择计算环境(训练&预测)"),
    OUTPUT_COMMON(5, "选择结果接收方(通用)"),
    OUTPUT_TRAINING_PREDICTION(6, "选择结果接收方(训练&预测)")
    ;

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    CalculationProcessTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer,CalculationProcessTypeEnum> map = new HashMap<>();
    static {
        for (CalculationProcessTypeEnum value : CalculationProcessTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static CalculationProcessTypeEnum find(Integer value) {
        return map.get(value);
    }
}
