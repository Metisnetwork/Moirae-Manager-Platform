package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum UserLoginStatusEnum {
    FAIL(0, "失败"),
    SUCCESS(1, "成功");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    UserLoginStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer, UserLoginStatusEnum> map = new HashMap<>();
    static {
        for (UserLoginStatusEnum value : UserLoginStatusEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static UserLoginStatusEnum find(Integer value) {
        return map.get(value);
    }
}
