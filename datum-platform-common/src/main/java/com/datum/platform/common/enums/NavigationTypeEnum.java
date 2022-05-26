package com.datum.platform.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NavigationTypeEnum {

    ORG("org", "组织"),
    TASK("task", "任务");

    @JsonValue
    private String value;
    private String desc;

    NavigationTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
