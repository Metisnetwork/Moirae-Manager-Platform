package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/9/10
 * @description 元数据是否已过期
 */
public enum MetaDataExpireStatusEnum {
    /**
     * 未过期
     */
    un_expire((byte) 0),
    /**
     * 已过期
     */
    expire((byte) 1);

    private final byte value;

    MetaDataExpireStatusEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
