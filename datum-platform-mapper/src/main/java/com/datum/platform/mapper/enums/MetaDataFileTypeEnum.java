package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum MetaDataFileTypeEnum {

    UNKNOWN(0, "未知"),
    CSV(1, "csv");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    MetaDataFileTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer,MetaDataFileTypeEnum> map = new HashMap<>();
    static {
        for (MetaDataFileTypeEnum value : MetaDataFileTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static MetaDataFileTypeEnum find(Integer value) {
        return map.get(value);
    }
}
