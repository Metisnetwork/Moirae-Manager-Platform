package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 用户元数据审核状态枚举
 */
public enum UserMetaDataAuditEnum {

    /**
     * 未知(1.未登录故获取不到元数据状态 2.用户未申请使用元数据)
     */
    AUDIT_UNKNOWN((byte) -1),
    /**
     * 等待审核中
     */
    AUDIT_PENDING((byte) 0),
    /**
     * 审核通过
     */
    AUDIT_PASSED((byte) 1),
    /**
     * 审核拒绝
     */
    AUDIT_REFUSED((byte) 2);

    private final byte value;

    UserMetaDataAuditEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
