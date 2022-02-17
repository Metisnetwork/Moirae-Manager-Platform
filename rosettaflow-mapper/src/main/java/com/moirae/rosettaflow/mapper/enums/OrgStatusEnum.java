package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrgStatusEnum {

    Normal(1),
    NonNormal(2);

    @EnumValue
    @JsonValue
    private Integer value;
    OrgStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static OrgStatusEnum getEnum(Integer value) {
        if(Normal.value == value){
            return Normal;
        }
        if(NonNormal.value == value){
            return NonNormal;
        }
        return null;
    }
}
