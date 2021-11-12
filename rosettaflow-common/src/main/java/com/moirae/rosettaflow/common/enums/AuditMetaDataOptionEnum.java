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
    AUDIT_PENDING((byte)0),
    /**
     * 审核通过
     */
    AUDIT_PASSED((byte)1),
    /**
     * 审核拒绝
     */
    AUDIT_REFUSED((byte)2);

    private final byte value;

    AuditMetaDataOptionEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
