package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkflowCreateModeEnum {

    expert_Mode("expertMode", "专家模式"),
    wizard_Mode("wizardMode", "向导模式");

    @JsonValue
    private String value;
    private String desc;

    WorkflowCreateModeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
