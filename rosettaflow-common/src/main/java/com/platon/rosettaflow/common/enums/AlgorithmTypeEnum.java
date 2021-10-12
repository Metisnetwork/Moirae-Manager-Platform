package com.platon.rosettaflow.common.enums;

/**
 * 算法类型
 *
 * @author admin
 * @date 2021/8/16
 */
public enum AlgorithmTypeEnum {

    /**
     * 统计分析
     */
    ALGORITHM_TYPE_1((byte) 1, "统计分析", "统计分析"),
    /**
     * 特征工程
     */
    ALGORITHM_TYPE_2((byte) 2, "特征工程", "特征工程"),
    /**
     * 机器学习
     */
    ALGORITHM_TYPE_3((byte) 3, "机器学习", "机器学习");

    private final byte type;
    private final String name;
    private final String desc;

    AlgorithmTypeEnum(byte type, String name, String desc) {
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    public static String getName(byte type) {
        for (AlgorithmTypeEnum e : values()) {
            if (e.type == type) {
                return e.getName();
            }
        }
        return null;
    }

    public static String getDesc(byte type) {
        for (AlgorithmTypeEnum e : values()) {
            if (e.type == type) {
                return e.getDesc();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
