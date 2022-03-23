package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkflowStatusEnum {

    PAY_NEED(0, "待支付"),
    PAY_DOING(1, "支付中"),
    PAY_SUCCESS(2, "已支付"),
    RUN_DOING(3, "运行中"),
    RUN_SUCCESS(4, "运行成功"),
    RUN_FAIL(5, "运行失败"),
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
