package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum MetaDataCertificateTypeEnum {

    NO_ATTRIBUTES(0, "无属性"),
    HAVE_ATTRIBUTES(1, "有属性");

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    MetaDataCertificateTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, MetaDataCertificateTypeEnum> map = new HashMap<>();
    static {
        for (MetaDataCertificateTypeEnum value : MetaDataCertificateTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static MetaDataCertificateTypeEnum find(Integer value) {
        return map.get(value);
    }
}
