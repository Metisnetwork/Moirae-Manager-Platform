package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 元数据文件类型枚举
 */
public enum MetaDataFileTypeEnum {
    /**
     * 未知
     */
    FileType_Unknown(0),
    /**
     * csv, 目前只支持这个.
     */
    FileType_CSV(1);

    private final int value;

    MetaDataFileTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
