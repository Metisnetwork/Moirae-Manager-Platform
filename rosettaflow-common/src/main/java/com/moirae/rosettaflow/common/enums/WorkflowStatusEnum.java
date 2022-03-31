package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkflowStatusEnum {

    RUN_NEED(0, "待运行"),
    RUN_DOING(1, "运行中"),
    RUN_SUCCESS(2, "运行成功"),
    RUN_FAIL(3, "运行失败"),
    ;

    @JsonValue
    private Integer value;
    private String desc;

    WorkflowStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }
}
