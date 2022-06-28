package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TokenTypeEnum {

    ERC20(0, "ERC2O"),
    ERC721(1, "ERC721")
    ;

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    TokenTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, TokenTypeEnum> map = new HashMap<>();
    static {
        for (TokenTypeEnum value : TokenTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static TokenTypeEnum find(Integer value) {
        return map.get(value);
    }
}
