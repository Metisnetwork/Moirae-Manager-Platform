package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum OrgStatusEnum {

    Normal(1, "正常"),
    NonNormal(2, "不正常");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    OrgStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer,OrgStatusEnum> map = new HashMap<>();
    static {
        for (OrgStatusEnum value : OrgStatusEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static OrgStatusEnum find(Integer value) {
        return map.get(value);
    }
}
