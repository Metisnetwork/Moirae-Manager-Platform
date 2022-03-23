package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.client.DataProviderServiceClient;
import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.service.DownloadReply;
import com.moirae.rosettaflow.grpc.service.GrpcDataProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.Consumer;


/**
 * @author juzix
 * @description 获取任务结果数据
 */
@Slf4j
@Service
public class GrpcDataProviderServiceImpl implements GrpcDataProviderService {

    @Resource
    private DataProviderServiceClient dataProviderServiceClient;

    @Override
    public void downloadTask(DownloadRequestDto requestDto, Consumer<DownloadReply> callback) {
         dataProviderServiceClient.getTaskDownload(requestDto, callback);
    }
}
