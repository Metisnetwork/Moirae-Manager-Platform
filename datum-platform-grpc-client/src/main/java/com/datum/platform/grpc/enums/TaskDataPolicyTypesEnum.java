package com.datum.platform.grpc.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TaskDataPolicyTypesEnum {
    POLICY_TYPES_0(0, "未知原始数据格式 (一般只有根据任务的结果文件由系统默认生成的元数据才会是这种格式)"),
    POLICY_TYPES_1(1, "csv格式原始数据 (无消费方式)"),
    POLICY_TYPES_2(2, "dir格式 (目录) (无消费方式)"),
    POLICY_TYPES_3(3, "binary格式 (普通的二进制数据, 没有明确说明后缀的二进制文件) (无消费方式)"),
    POLICY_TYPES_4(4, "xls格式 (无消费方式)"),
    POLICY_TYPES_5(5, "xlsx格式 (无消费方式)"),
    POLICY_TYPES_6(6, "txt格式 (无消费方式)"),
    POLICY_TYPES_7(7, "json格式 (无消费方式)"),
    POLICY_TYPES_30001(30001, "csv格式原始数据 (基于任务的结果文件)"),
    POLICY_TYPES_30002(30002, "dir格式 (目录) (基于任务的结果文件)"),
    POLICY_TYPES_30003(30003, "binary格式 (普通的二进制数据, 没有明确说明后缀的二进制文件) (基于任务的结果文件)"),
    POLICY_TYPES_30004(30004, "xls格式 (基于任务的结果文件)"),
    POLICY_TYPES_30005(30005, "xlsx格式 (基于任务的结果文件)"),
    POLICY_TYPES_30006(30006, "txt格式 (基于任务的结果文件)"),
    POLICY_TYPES_30007(30007, "json格式 (基于任务的结果文件)"),
    POLICY_TYPES_40001(40001, "csv格式原始数据 (有消费方式)"),
    POLICY_TYPES_40002(40002, "dir格式 (目录) (有消费方式)"),
    POLICY_TYPES_40003(40003, "binary格式 (普通的二进制数据, 没有明确说明后缀的二进制文件) (有消费方式)"),
    POLICY_TYPES_40004(40004, "xls格式 (有消费方式)"),
    POLICY_TYPES_40005(40005, "xlsx格式 (有消费方式)"),
    POLICY_TYPES_40006(40006, "txt格式 (有消费方式)"),
    POLICY_TYPES_40007(40007, "json格式 (有消费方式)"),;

    private Integer value;
    private String desc;

    TaskDataPolicyTypesEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, TaskDataPolicyTypesEnum> map = new HashMap<>();
    static {
        for (TaskDataPolicyTypesEnum value : TaskDataPolicyTypesEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static TaskDataPolicyTypesEnum find(Integer value) {
        return map.get(value);
    }
}
