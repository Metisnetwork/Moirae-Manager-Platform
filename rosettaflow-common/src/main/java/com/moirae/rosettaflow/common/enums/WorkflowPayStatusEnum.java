package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkflowPayStatusEnum {

    PAY_NEED(0, "待支付"),
    PAY_DOING(1, "支付中"),
    PAY_SUCCESS(2, "支付成功"),
    PAY_FAIL(3, "支付失败"),
    REVOKE_DOING(4, "撤销中"),
    REVOKE_SUCCESS(5, "撤销成功"),
    REVOKE_FAIL(6, "撤销失败"),
    ;

    @JsonValue
    private Integer value;
    private String desc;

    WorkflowPayStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }
}
