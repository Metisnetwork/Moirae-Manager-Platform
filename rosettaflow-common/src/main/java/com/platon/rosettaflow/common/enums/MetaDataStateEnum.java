package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 元数据文件类型枚举
 */
public enum MetaDataStateEnum {
    /**
     * 未知
     */
    MetaDataState_Unknown(0),
    /**
     * 1-还未发布的新表
     */
    MetaDataState_Created(1),
    /**
     * 2-已发布的表
     */
    MetaDataState_Released(2),
    /**
     * 3-已撤销的表
     */
    MetaDataState_Revoked(3);

    private final int value;

    MetaDataStateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
