package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum OrgOrderByEnum {

    NAME("name", "o.node_name", "名称"),
    TOKEN_COUNT("tokenCount", "so.total_data_token desc", "凭证数据量"),
    TASK_COUNT("taskCount","so.total_task desc", "参与任务数"),
    CORE("core", "so.org_total_core desc","总CPU"),
    MEMORY("memory","so.org_total_memory desc", "总内存"),
    BANDWIDTH("bandwidth", "so.total_data_token desc","总带宽"),
    ;

    @JsonValue
    private String userValue;
    private String sqlValue;
    private String desc;

    OrgOrderByEnum(String userValue, String sqlValue, String desc) {
        this.userValue = userValue;
        this.sqlValue = sqlValue;
        this.desc = desc;
    }

    public String getUserValue() {
        return this.userValue;
    }

    public String getSqlValue(){
        return this.sqlValue;
    }

    private static Map<String,OrgOrderByEnum> map = new HashMap<>();
    static {
        for (OrgOrderByEnum value : OrgOrderByEnum.values()) {
            map.put(value.getUserValue(),value);
        }
    }
    public static OrgOrderByEnum find(String value) {
        return map.get(value);
    }
}
