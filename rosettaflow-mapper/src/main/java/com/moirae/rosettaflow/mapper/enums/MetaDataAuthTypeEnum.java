package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum MetaDataAuthTypeEnum {

    PENDING(0, "未定义类型"),
    PERIOD(1, "依照时间段来使用"),
    TIMES(2, "依照次数来使用");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    MetaDataAuthTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer, MetaDataAuthTypeEnum> map = new HashMap<>();
    static {
        for (MetaDataAuthTypeEnum value : MetaDataAuthTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static MetaDataAuthTypeEnum find(Integer value) {
        return map.get(value);
    }
}
