package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum AlgorithmTypeEnum {

    CT(0, "密文"),
    PT(1, "明文");

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    AlgorithmTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, AlgorithmTypeEnum> map = new HashMap<>();
    static {
        for (AlgorithmTypeEnum value : AlgorithmTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static AlgorithmTypeEnum find(Integer value) {
        return map.get(value);
    }
}
