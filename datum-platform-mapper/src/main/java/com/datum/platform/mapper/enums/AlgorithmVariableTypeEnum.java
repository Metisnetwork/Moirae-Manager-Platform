package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum AlgorithmVariableTypeEnum {

    BOOLEAN(1, "boolean"),
    NUMBER(2, "number"),
    STRING(3, "string"),
    NUMBER_ARRAY(4, "numberArray"),
    STRING_ARRAY(5, "stringArray"),
            ;
    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    AlgorithmVariableTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer, AlgorithmVariableTypeEnum> map = new HashMap<>();
    static {
        for (AlgorithmVariableTypeEnum value : AlgorithmVariableTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static AlgorithmVariableTypeEnum find(Integer value) {
        return map.get(value);
    }
}
