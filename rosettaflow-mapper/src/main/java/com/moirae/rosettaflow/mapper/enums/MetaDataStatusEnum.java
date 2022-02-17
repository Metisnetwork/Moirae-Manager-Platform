package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum MetaDataStatusEnum {

    UNKNOWN(0, "未知"),
    UNPUBLISHED(1, "未发布"),
    PUBLISHED(2, "已发布"),
    REVOKED(3, "已撤销");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    MetaDataStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer,MetaDataStatusEnum> map = new HashMap<>();
    static {
        for (MetaDataStatusEnum value : MetaDataStatusEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static MetaDataStatusEnum find(Integer value) {
        return map.get(value);
    }
}
