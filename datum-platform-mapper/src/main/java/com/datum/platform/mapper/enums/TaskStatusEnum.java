package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatusEnum {

    UNKNOWN(0, "未知"),
    PENDING(1, "等在中"),
    RUNNING(2, "计算中"),
    FAILED(3, "失败"),
    SUCCEED(4, "成功");

    @EnumValue
    @JsonValue
    private Integer value;
    private String desc;

    TaskStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer, TaskStatusEnum> map = new HashMap<>();
    static {
        for (TaskStatusEnum value : TaskStatusEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static TaskStatusEnum find(Integer value) {
        return map.get(value);
    }
}
