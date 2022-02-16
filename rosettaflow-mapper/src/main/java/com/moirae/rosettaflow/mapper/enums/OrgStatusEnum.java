package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum OrgStatusEnum {

    Normal(1),
    NonNormal(2);

    @EnumValue
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
