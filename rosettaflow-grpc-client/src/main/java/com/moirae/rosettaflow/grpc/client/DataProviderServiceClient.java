package com.moirae.rosettaflow.grpc.client;


import com.google.protobuf.ByteString;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.TaskDownloadStatusEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.data.provider.resp.dto.DownloadReplyResponseDto;
import com.moirae.rosettaflow.grpc.service.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author admin
 * @date 2021/11/03
 * @description 数据提供接口 (data_svc.proto)
 */
@Slf4j
@Component
public class DataProviderServiceClient {

    @GrpcClient("carrier-grpc-server")
    DataProviderGrpc.DataProviderStub dataProviderStub;



    /**
     * 下载任务结果
     * @param requestDto : 任务结果下载入参
     * @return List<DownloadReplyResponseDto> : 任务结果数据文件
     */
    public DownloadReplyResponseDto getTaskDownload(DownloadRequestDto requestDto) {
        DownloadReplyResponseDto responseDto = new DownloadReplyResponseDto();
        DownloadRequest downloadRequest = DownloadRequest.newBuilder()
                        .setFilePath(requestDto.getFilePath())
                        .putOptions("compress", requestDto.getCompress().get("compress"))
                        .build();
        dataProviderStub.downloadData(downloadRequest, new StreamObserver<DownloadReply>() {

            @Override
            public void onNext(DownloadReply downloadReply) {
                if (downloadReply.hasStatus()) {
                    int status = downloadReply.getStatus().getNumber();
                    if (status == TaskDownloadStatusEnum.Failed.getValue()) {
                        log.info("download task result failed....,filePath:{}",requestDto.getFilePath());
                        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_RESULT_DOWNLOAD_FAIL.getMsg());
                    }
                    if (downloadReply.hasContent()) {
                        ByteString downloadContent = downloadReply.getContent();
                        responseDto.setContent(downloadContent.toByteArray());
                        responseDto.setDownloadStatus(status);
                    }

                    switch (status) {
                        case 0:
                            log.info("download task result start,filePath:{}",requestDto.getFilePath());
                            break;
                        case 1:
                            log.info("download task result finish,filePath:{}",requestDto.getFilePath());
                            break;
                        case 2:
                            log.info("download task result cancel,filePath:{}",requestDto.getFilePath());
                            break;
                        default:
                            log.info("download task result status error,filePath:{}",requestDto.getFilePath());
                            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_RESULT_DOWNLOAD_ERROR.getMsg());
                    }
                } else {
                    log.info("download task result status error,filePath:{}",requestDto.getFilePath());
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_RESULT_DOWNLOAD_ERROR.getMsg());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Data result download error, filePath:{}, reason:{}", requestDto.getFilePath(), throwable.getMessage());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_RESULT_DOWNLOAD_ERROR.getMsg());
            }

            @Override
            public void onCompleted() {
                log.error("Data result download completed, filePath:{}", requestDto.getFilePath());
            }
        });
        return responseDto;
    }





}
