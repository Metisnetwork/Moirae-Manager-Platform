package com.datum.platform.grpc.client.impl;

import carrier.api.SysRpcApi;
import carrier.api.YarnServiceGrpc;
import com.datum.platform.common.enums.ErrorMsg;
import com.datum.platform.common.enums.RespCodeEnum;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.grpc.client.GrpcSysServiceClient;
import com.datum.platform.grpc.constant.GrpcConstant;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

@Slf4j
@Component
public class GrpcSysServiceClientImpl implements GrpcSysServiceClient {

    @GrpcClient("carrier-grpc-server")
    YarnServiceGrpc.YarnServiceBlockingStub yarnServiceBlockingStub;

    public void downloadTaskResultData(Channel channel, OutputStream outputStream, String taskId) throws IOException {
        SysRpcApi.DownloadTaskResultDataRequest request = SysRpcApi.DownloadTaskResultDataRequest.newBuilder()
                .setTaskId(taskId)
                .putOptions("file_root_dir", "result")
                .putOptions("compress", "zip")
                .build();
        Iterator<SysRpcApi.DownloadTaskResultDataResponse> response = YarnServiceGrpc.newBlockingStub(channel).downloadTaskResultData(request);
        response.forEachRemaining(downloadTaskResultDataResponse -> {
            if(downloadTaskResultDataResponse.hasContent()){
                try {
                    outputStream.write(downloadTaskResultDataResponse.getContent().toByteArray());
                } catch (IOException e) {
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_RESULT_DOWNLOAD_ERROR.getMsg(), e);
                }
            }
            downloadTaskResultDataResponse.getContent().toByteArray();
        });
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public SysRpcApi.GetTaskResultFileSummary getTaskResultFileSummary(Channel channel, String taskId) {
        SysRpcApi.GetTaskResultFileSummaryRequest request = SysRpcApi.GetTaskResultFileSummaryRequest.newBuilder()
                .setTaskId(taskId)
                .build();

        SysRpcApi.GetTaskResultFileSummaryResponse response = YarnServiceGrpc.newBlockingStub(channel).getTaskResultFileSummary(request);
        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcAuthServiceClientImpl->getIdentityList() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }

        return response.getInformation();
    }
}
