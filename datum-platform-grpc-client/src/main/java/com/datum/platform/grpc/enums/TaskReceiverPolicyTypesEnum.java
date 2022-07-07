package com.datum.platform.grpc.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TaskReceiverPolicyTypesEnum {
    POLICY_TYPES_1(1, "指定接收方标签名的单组织内部随机选举数据节点策略"),
    POLICY_TYPES_2(2, "指定数据节点提供作为接收方策略"),
    ;

    private Integer value;
    private String desc;

    TaskReceiverPolicyTypesEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, TaskReceiverPolicyTypesEnum> map = new HashMap<>();
    static {
        for (TaskReceiverPolicyTypesEnum value : TaskReceiverPolicyTypesEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static TaskReceiverPolicyTypesEnum find(Integer value) {
        return map.get(value);
    }
}
