package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrgOrderByEnum {

    NAME("name", "名称"),
    TOKEN_COUNT("tokenCount", "凭证数据量"),
    TASK_COUNT("taskCount", "参与任务数"),
    CORE("core", "总CPU"),
    MEMORY("memory", "总内存"),
    BANDWIDTH("bandwidth", "总带宽"),
    ;

    @JsonValue
    private String value;
    private String desc;

    OrgOrderByEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
