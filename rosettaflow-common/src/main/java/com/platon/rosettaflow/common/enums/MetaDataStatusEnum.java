package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据状态枚举
 */
public enum MetaDataStatusEnum {
    /**
     * 还未发布的新表
     */
    CREATE("create"),
    /**
     * 已发布的表
     */
    RELEASE("release"),
    /**
     * 已撤销的表
     */
    REVOKE("revoke");

    private final String value;

    MetaDataStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
