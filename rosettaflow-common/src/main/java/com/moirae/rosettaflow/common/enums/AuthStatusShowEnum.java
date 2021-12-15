package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 元数据申请授权状态(用于前端展示状态使用)
 */
public enum AuthStatusShowEnum {

    /**
     * 未知
     */
    UNKNOWN((byte) -1),
    /**
     * 申请中
     */
    APPLY((byte) 0),
    /**
     * 已授权
     */
    AUTHORIZED((byte) 1),
    /**
     * 已拒绝
     */
    REFUSE((byte) 2),
    /**
     * 已撤销
     */
    CANCELED((byte) 3),
    /**
     * 已失效
     */
    INVALID((byte) 4);

    private final byte value;

    AuthStatusShowEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
