package com.moirae.rosettaflow.grpc.client;


import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.data.provider.resp.dto.DownloadReplyResponseDto;
import com.moirae.rosettaflow.grpc.service.*;
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
    DataProviderGrpc.DataProviderBlockingStub dataProviderBlockingStub;



    /**
     * 下载任务结果
     * @param requestDto : 任务结果下载入参
     * @return List<DownloadReplyResponseDto> : 任务结果数据文件
     */
    public List<DownloadReplyResponseDto> getTaskDownload(DownloadRequestDto requestDto){
        List<DownloadReplyResponseDto> downloadReplyResponseDtoList = new ArrayList<>();
        DownloadRequest downloadRequest = DownloadRequest.newBuilder()
                        .setFilePath(requestDto.getFilePath())
                        .putOptions("compress", requestDto.getCompress().get("compress"))
                        .build();

        Iterator<DownloadReply> downloadReplyResponse = dataProviderBlockingStub.downloadData(downloadRequest);

        while (downloadReplyResponse.hasNext()) {
            DownloadReply downloadReply = downloadReplyResponse.next();

            DownloadReplyResponseDto responseDto = new DownloadReplyResponseDto();
            responseDto.setContent( downloadReply.getContent().toByteArray());
            responseDto.setDownloadStatus(downloadReply.getStatus().getNumber());
            downloadReplyResponseDtoList.add(responseDto);
        }
        return downloadReplyResponseDtoList;
    }





}
