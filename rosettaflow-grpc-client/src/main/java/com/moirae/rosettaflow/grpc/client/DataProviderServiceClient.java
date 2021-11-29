package com.moirae.rosettaflow.grpc.client;

import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.data.provider.resp.dto.DownloadReplyResponseDto;
import com.moirae.rosettaflow.grpc.service.DataProviderGrpc;
import com.moirae.rosettaflow.grpc.service.DownloadReply;
import com.moirae.rosettaflow.grpc.service.DownloadRequest;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author admin
 * @date 2021/11/03
 * @description 数据提供接口 (data_svc.proto)
 */
@Slf4j
@Component
public class DataProviderServiceClient {

   /* @GrpcClient("carrier-grpc-server")
    DataProviderGrpc.DataProviderBlockingStub dataProviderBlockingStub;*/


    /**
     * 下载任务结果
     *
     * @param requestDto : 任务结果下载入参
     */
    public void getTaskDownload(DownloadRequestDto requestDto, Consumer<DownloadReplyResponseDto> callback) {

        Channel channel;
        try {
            //1.获取连接
            channel = ManagedChannelBuilder
                    .forAddress(requestDto.getIp(), requestDto.getPort())
                    .usePlaintext()
                    .keepAliveWithoutCalls(true)
                    .build();
            //2.构建请求
            DownloadRequest downloadRequest = DownloadRequest.newBuilder()
                    .setFilePath(requestDto.getFilePath())
                    .putOptions("compress", requestDto.getCompress().get("compress"))
                    .build();
            //3.调用下载
            DataProviderGrpc.newStub(channel).downloadData(downloadRequest, new StreamObserver<DownloadReply>() {
                @Override
                public void onNext(DownloadReply downloadReply) {
                    boolean hasContent = downloadReply.hasContent();
                    int taskStatus = downloadReply.getStatus().getNumber();

                    log.error("Downloading metadata result file..., filePath:{}, taskStatus:{}, hasContent:{}", downloadRequest.getFilePath(), taskStatus, hasContent);
                    DownloadReplyResponseDto responseDto = new DownloadReplyResponseDto();
                    responseDto.setDownloadStatus(taskStatus);
                    responseDto.setContent(hasContent ? downloadReply.getContent().toByteArray() : null);
                    if (Objects.nonNull(callback)) {
                        callback.accept(responseDto);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("Download metadata result file fail, filePath:{}, fail reason:{}", downloadRequest.getFilePath(), throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    log.error("Download metadata result file finish, filePath:{}", downloadRequest.getFilePath());
                }
            });
        } catch (Exception e) {
            log.error("Download metadata result file fail, fail reason:{}", e.getMessage());
        }
    }


}
