package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum MetaDataAuthStatusEnum {

    UNKNOWN(0, "未知"),
    UNPUBLISHED(1, "未发布"),
    PUBLISHED(2, "已发布"),
    REVOKED(3, "已撤销"),
    INVALID(4, "已失效");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    MetaDataAuthStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer, MetaDataAuthStatusEnum> map = new HashMap<>();
    static {
        for (MetaDataAuthStatusEnum value : MetaDataAuthStatusEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static MetaDataAuthStatusEnum find(Integer value) {
        return map.get(value);
    }
}
