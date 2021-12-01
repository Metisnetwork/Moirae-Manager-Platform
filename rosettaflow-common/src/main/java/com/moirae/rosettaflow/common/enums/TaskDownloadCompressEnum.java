package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 任务结果下载文件压缩格式
 */
public enum TaskDownloadCompressEnum {
    /**
     * 压缩文件zip格式
     */
    ZIP(1, "zip"),
    /**
     * 压缩文件tar.gz格式
     */
    TAR_GZ(2,"tar.gz");

    private final int value;
    private final String compressType;

    TaskDownloadCompressEnum(int value, String msg) {
        this.value = value;
        this.compressType = msg;
    }

    public static TaskDownloadCompressEnum getByValue(int value) {
        for (TaskDownloadCompressEnum e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getCompressType() {
        return compressType;
    }
}
