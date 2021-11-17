package com.moirae.rosettaflow.grpc.service;

import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.data.provider.resp.dto.DownloadReplyResponseDto;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
public interface GrpcDataProviderService {

    /**
     * 下载任务结果
     * @param requestDto : 任务结果下载入参
     * @return DownloadReplyResponseDto : 任务结果数据文件
     */
    DownloadReplyResponseDto downloadTask(DownloadRequestDto requestDto);

}
