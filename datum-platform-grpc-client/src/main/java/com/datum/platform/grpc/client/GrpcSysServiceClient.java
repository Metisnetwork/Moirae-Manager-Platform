package com.datum.platform.grpc.client;

import carrier.api.SysRpcApi;
import io.grpc.Channel;

import java.io.IOException;
import java.io.OutputStream;

public interface GrpcSysServiceClient {

    /**
     * 查询指定任务的结果摘要
     *
     * @param channel 下载文件的渠道
     * @param taskId  任务id
     * @return 任务结果文件摘要
     */
    SysRpcApi.GetTaskResultFileSummary getTaskResultFileSummary(Channel channel, String taskId);


    void downloadTaskResultData(Channel channel, OutputStream outputStream, String taskId) throws IOException;

}
