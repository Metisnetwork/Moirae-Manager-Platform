package com.moirae.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 任务结果下载状态
 */
public enum TaskDownloadStatusEnum {
    /**
     * 开始
     */
    Start(0),
    /**
     * 完成
     */
    Finished((byte) 1),
    /**
     * 取消
     */
    Cancelled((byte) 2),
    /**
     * 失败
     */
    Failed((byte) 3);


    private final int value;

    TaskDownloadStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
