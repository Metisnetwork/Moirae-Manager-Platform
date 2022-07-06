package com.datum.platform.grpc.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum PowerPolicyTypesEnum {
    POLICY_TYPES_1(1, "指定标签名的随机选举策略"),
    POLICY_TYPES_2(2, "指定数据节点提供算力策略"),
    POLICY_TYPES_3(3, "指定组织选提供算力策略"),
    ;

    private Integer value;
    private String desc;

    PowerPolicyTypesEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, PowerPolicyTypesEnum> map = new HashMap<>();
    static {
        for (PowerPolicyTypesEnum value : PowerPolicyTypesEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static PowerPolicyTypesEnum find(Integer value) {
        return map.get(value);
    }
}
