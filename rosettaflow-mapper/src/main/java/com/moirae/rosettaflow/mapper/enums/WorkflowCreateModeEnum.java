package com.moirae.rosettaflow.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum WorkflowCreateModeEnum {

    EXPERT_MODE(1, "专家模式"),
    WIZARD_MODE(2, "向导模式");

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    WorkflowCreateModeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
