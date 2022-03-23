package com.moirae.rosettaflow.grpc.data.provider.resp.dto;

import lombok.Data;

/**
 * @author juzix
 * @description 任务结果下载数据
 */
@Data
public class DownloadReplyResponseDto {
    /**
     * 任务结果下载状态: 0、Start，1、Finished，2、Cancelled，3、Failed
     */
    int downloadStatus;
    /**
     * 任务结果内容
     */
    byte[] content;
    /**
     * 下载文件名称
     */
    String fileName;
}
