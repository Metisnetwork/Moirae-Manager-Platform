package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatusEnum {

    ALL(null, "全部"),
    SUCCESS(4, "成功"),
    FAIL(3, "失败");

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
}
