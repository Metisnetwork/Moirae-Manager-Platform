package com.datum.platform.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkflowPayTypeEnum {

    FEE(0, "主币支付"),
    TOKEN(1, "代币支付")
    ;

    @JsonValue
    private Integer value;
    private String desc;

    WorkflowPayTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }
}
