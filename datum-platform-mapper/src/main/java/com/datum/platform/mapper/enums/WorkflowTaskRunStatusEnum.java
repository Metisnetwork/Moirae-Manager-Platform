package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum WorkflowTaskRunStatusEnum {

    RUN_NEED(0, "待运行"),
    RUN_DOING(1, "运行中"),
    RUN_SUCCESS(2, "运行成功"),
    RUN_FAIL(3, "运行失败"),

    RUN_SUSPEND(4, "中止运行"),
    ;

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    WorkflowTaskRunStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, WorkflowTaskRunStatusEnum> map = new HashMap<>();
    static {
        for (WorkflowTaskRunStatusEnum value : WorkflowTaskRunStatusEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static WorkflowTaskRunStatusEnum find(Integer value) {
        return map.get(value);
    }
}
