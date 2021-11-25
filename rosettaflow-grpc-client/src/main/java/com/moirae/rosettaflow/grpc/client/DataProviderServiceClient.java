package com.moirae.rosettaflow.grpc.client;


import cn.hutool.core.bean.BeanUtil;
import com.google.protobuf.ByteString;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.TaskDownloadStatusEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.data.provider.resp.dto.DownloadReplyResponseDto;
import com.moirae.rosettaflow.grpc.service.DataProviderGrpc;
import com.moirae.rosettaflow.grpc.service.DownloadReply;
import com.moirae.rosettaflow.grpc.service.DownloadRequest;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * @author admin
 * @date 2021/11/03
 * @description 数据提供接口 (data_svc.proto)
 */
@Slf4j
@Component
public class DataProviderServiceClient {

    @GrpcClient("carrier-grpc-server")
    DataProviderGrpc.DataProviderBlockingStub dataProviderBlockingStub;


    /**
     * 下载任务结果
     *
     * @param requestDto : 任务结果下载入参
     * @return List<DownloadReplyResponseDto> : 任务结果数据文件
     */
    public DownloadReplyResponseDto getTaskDownload(DownloadRequestDto requestDto) {
        DownloadReplyResponseDto responseDto = new DownloadReplyResponseDto();
        DownloadRequest downloadRequest = DownloadRequest.newBuilder()
                .setFilePath(requestDto.getFilePath())
                .putOptions("compress", requestDto.getCompress().get("compress"))
                .build();

        Iterator<DownloadReply> downloadReplyIterator = dataProviderBlockingStub.downloadData(downloadRequest);
        while (downloadReplyIterator.hasNext()){
            DownloadReply downloadReply = downloadReplyIterator.next();
            BeanUtil.copyProperties(downloadReply,responseDto);
        }
        return responseDto;
    }


}
