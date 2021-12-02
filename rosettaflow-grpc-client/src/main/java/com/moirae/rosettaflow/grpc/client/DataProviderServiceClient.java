package com.moirae.rosettaflow.grpc.client;

import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.service.DataProviderGrpc;
import com.moirae.rosettaflow.grpc.service.DownloadReply;
import com.moirae.rosettaflow.grpc.service.DownloadRequest;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.function.Consumer;

/**
 * @author admin
 * @date 2021/11/03
 * @description 数据提供接口 (data_svc.proto)
 */
@Slf4j
@Component
public class DataProviderServiceClient {
    /**
     * 下载任务结果
     *
     * @param requestDto : 任务结果下载入参
     */
    public void getTaskDownload(DownloadRequestDto requestDto, Consumer<DownloadReply> callback) {
        Channel channel;
        try {
            //1.获取连接
            channel = ManagedChannelBuilder
                    .forAddress(requestDto.getIp(), Integer.parseInt(requestDto.getPort()))
                    .usePlaintext()
                    .build();
            //2.构建请求
            DownloadRequest downloadRequest = DownloadRequest.newBuilder()
                    .setFilePath(requestDto.getFilePath())
                    .putOptions("compress", requestDto.getCompress().get("compress"))
                    .build();
            //3.调用下载
            StreamObserver<DownloadReply> responseObserver = new StreamObserver<DownloadReply>() {
                @Override
                public void onNext(DownloadReply downloadReply) {
                    callback.accept(downloadReply);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("Download metadata result file fail, filePath:{}, fail reason:{}", downloadRequest.getFilePath(), throwable.getMessage());
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_FILE_DOWNLOAD_FAIL.getMsg());
                }

                @Override
                public void onCompleted() {
                    log.error("Download metadata result file finish, filePath:{}", downloadRequest.getFilePath());
                }
            };
            DataProviderGrpc.newStub(channel).downloadData(downloadRequest, responseObserver);

        } catch (Exception e) {
            log.error("Download metadata result file fail, fail reason:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_FILE_DOWNLOAD_FAIL.getMsg());
        }
    }


}
