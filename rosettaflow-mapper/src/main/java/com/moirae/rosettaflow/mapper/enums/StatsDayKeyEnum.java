package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.HashMap;
import java.util.Map;

public enum StatsDayKeyEnum {

    TASK_COUNT("taskCount", "计算走势");

    @EnumValue
    private String value;
    private String desc;

    StatsDayKeyEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    private static Map<String, StatsDayKeyEnum> map = new HashMap<>();
    static {
        for (StatsDayKeyEnum value : StatsDayKeyEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static StatsDayKeyEnum find(Integer value) {
        return map.get(value);
    }
}
