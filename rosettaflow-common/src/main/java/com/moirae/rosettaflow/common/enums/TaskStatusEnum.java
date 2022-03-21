package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatusEnum {

    ALL(0, "全部"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败");

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
