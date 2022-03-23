package com.moirae.rosettaflow.grpc.service;

import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;

import java.util.function.Consumer;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
public interface GrpcDataProviderService {

    /**
     * 下载任务结果
     * @param requestDto : 任务结果下载入参
     * @param callback : 任务结果下载回调
     */
    void downloadTask(DownloadRequestDto requestDto, Consumer<DownloadReply> callback);

}
