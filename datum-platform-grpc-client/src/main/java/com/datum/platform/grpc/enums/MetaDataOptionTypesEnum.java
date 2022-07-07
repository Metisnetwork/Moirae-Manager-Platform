package com.datum.platform.grpc.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum MetaDataOptionTypesEnum {
    POLICY_TYPES_1(1, "csv格式原始数据 (无消费方式)"),
    POLICY_TYPES_2(2, "dir格式 (目录) (无消费方式)"),
    POLICY_TYPES_3(3, "binary格式 (普通的二进制数据, 没有明确说明后缀的二进制文件) (无消费方式)"),
    POLICY_TYPES_4(4, "xls格式 (无消费方式)"),
    POLICY_TYPES_5(5, "xlsx格式 (无消费方式)"),
    POLICY_TYPES_6(6, "txt格式 (无消费方式)"),
    POLICY_TYPES_7(7, "json格式 (无消费方式)"),
    POLICY_TYPES_40001(40001, "csv格式原始数据 (有消费方式)"),
    POLICY_TYPES_40002(40002, "dir格式 (目录) (有消费方式)"),
    POLICY_TYPES_40003(40003, "binary格式 (普通的二进制数据, 没有明确说明后缀的二进制文件) (有消费方式)"),
    POLICY_TYPES_40004(40004, "xls格式 (有消费方式)"),
    POLICY_TYPES_40005(40005, "xlsx格式 (有消费方式)"),
    POLICY_TYPES_40006(40006, "txt格式 (有消费方式)"),
    POLICY_TYPES_40007(40007, "json格式 (有消费方式)"),;

    private Integer value;
    private String desc;

    MetaDataOptionTypesEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, MetaDataOptionTypesEnum> map = new HashMap<>();
    static {
        for (MetaDataOptionTypesEnum value : MetaDataOptionTypesEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static MetaDataOptionTypesEnum find(Integer value) {
        return map.get(value);
    }
}
