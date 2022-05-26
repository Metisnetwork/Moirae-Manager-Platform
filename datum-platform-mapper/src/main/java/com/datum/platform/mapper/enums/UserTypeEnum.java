package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum UserTypeEnum {

    UNDEFINED(0, "未定义"),
    SECOND_ADDRESS(1, "第二地址"),
    TESTNET_ADDRESS(2, "测试网地址"),
    MAINNET_ADDRESS(3, "主网地址");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    UserTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer, UserTypeEnum> map = new HashMap<>();
    static {
        for (UserTypeEnum value : UserTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static UserTypeEnum find(Integer value) {
        return map.get(value);
    }
}
