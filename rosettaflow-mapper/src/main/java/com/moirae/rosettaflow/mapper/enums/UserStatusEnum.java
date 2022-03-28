package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum UserStatusEnum {
    INVALID(0, "无效"),
    VALID(1, "有效");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    UserStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer,UserStatusEnum> map = new HashMap<>();
    static {
        for (UserStatusEnum value : UserStatusEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static UserStatusEnum find(Integer value) {
        return map.get(value);
    }
}
