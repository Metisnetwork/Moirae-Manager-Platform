package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 元数据审核结果枚举
 */
public enum AuditMetaDataOptionEnum {
    /**
     * 等待审核中
     */
    AUDIT_PENDING(0),
    /**
     * 审核通过
     */
    AUDIT_PASSED(1),
    /**
     * 审核拒绝
     */
    AUDIT_REFUSED(2);

    private final int value;

    AuditMetaDataOptionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
