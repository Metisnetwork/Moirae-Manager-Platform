package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum WorkflowTaskPowerTypeEnum {

    RANDOM(0, "随机一个"),
    ASSIGN(1, "指定一个");

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    WorkflowTaskPowerTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, WorkflowTaskPowerTypeEnum> map = new HashMap<>();
    static {
        for (WorkflowTaskPowerTypeEnum value : WorkflowTaskPowerTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static WorkflowTaskPowerTypeEnum find(Integer value) {
        return map.get(value);
    }
}
