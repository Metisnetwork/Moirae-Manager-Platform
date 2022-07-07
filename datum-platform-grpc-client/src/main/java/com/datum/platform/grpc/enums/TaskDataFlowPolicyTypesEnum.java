package com.datum.platform.grpc.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TaskDataFlowPolicyTypesEnum {
    POLICY_TYPES_1(1, "全连接策略"),
    POLICY_TYPES_2(2, "指定方向的连接策略"),
    ;

    private Integer value;
    private String desc;

    TaskDataFlowPolicyTypesEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, TaskDataFlowPolicyTypesEnum> map = new HashMap<>();
    static {
        for (TaskDataFlowPolicyTypesEnum value : TaskDataFlowPolicyTypesEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static TaskDataFlowPolicyTypesEnum find(Integer value) {
        return map.get(value);
    }
}
