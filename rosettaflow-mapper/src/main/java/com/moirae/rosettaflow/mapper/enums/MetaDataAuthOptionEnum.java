package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum MetaDataAuthOptionEnum {

    PENDING(0, "等待审核中"),
    PASSED(1, "审核通过"),
    REFUSED(2, "审核拒绝");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    MetaDataAuthOptionEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer, MetaDataAuthOptionEnum> map = new HashMap<>();
    static {
        for (MetaDataAuthOptionEnum value : MetaDataAuthOptionEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static MetaDataAuthOptionEnum find(Integer value) {
        return map.get(value);
    }
}
