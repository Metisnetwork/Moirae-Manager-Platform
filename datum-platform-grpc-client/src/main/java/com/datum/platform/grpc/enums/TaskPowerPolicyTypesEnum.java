package com.datum.platform.grpc.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TaskPowerPolicyTypesEnum {
    POLICY_TYPES_1(1, "指定标签名的随机选举策略"),
    POLICY_TYPES_2(2, "指定数据节点提供算力策略"),
    POLICY_TYPES_3(3, "指定组织选提供算力策略"),
    ;

    private Integer value;
    private String desc;

    TaskPowerPolicyTypesEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, TaskPowerPolicyTypesEnum> map = new HashMap<>();
    static {
        for (TaskPowerPolicyTypesEnum value : TaskPowerPolicyTypesEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static TaskPowerPolicyTypesEnum find(Integer value) {
        return map.get(value);
    }
}
