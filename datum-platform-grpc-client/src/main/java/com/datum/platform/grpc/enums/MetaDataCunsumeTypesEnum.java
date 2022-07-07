package com.datum.platform.grpc.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum MetaDataCunsumeTypesEnum {
    TYPES_1(1, "metadataAuth 消费"),
    TYPES_2(2, "ERC20 消费"),
    TYPES_3(3, "ERC721 消费"),
    ;

    private Integer value;
    private String desc;

    MetaDataCunsumeTypesEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, MetaDataCunsumeTypesEnum> map = new HashMap<>();
    static {
        for (MetaDataCunsumeTypesEnum value : MetaDataCunsumeTypesEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static MetaDataCunsumeTypesEnum find(Integer value) {
        return map.get(value);
    }
}
