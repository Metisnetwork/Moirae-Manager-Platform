package com.moirae.rosettaflow.grpc.data.provider.resp.dto;

import com.moirae.rosettaflow.common.enums.TaskDownloadStatusEnum;
import lombok.Data;

/**
 * @author juzix
 * @description 任务结果下载数据
 */
@Data
public class DownloadReplyResponseDto {

    /**
     * 任务结果下载状态
     */
    int downloadStatus;

    /**
     * 任务结果内容
     */
    byte[] content;


}
