package com.moirae.rosettaflow.grpc.client;

import com.moirae.rosettaflow.grpc.service.GetTaskResultFileSummary;
import com.moirae.rosettaflow.grpc.service.YarnNodeInfo;
import io.grpc.Channel;

public interface GrpcSysServiceClient {
    YarnNodeInfo getNodeInfo(Channel channel);

    /**
     * 查询指定任务的结果摘要
     *
     * @param channel 下载文件的渠道
     * @param taskId  任务id
     * @return 任务结果文件摘要
     */
    GetTaskResultFileSummary getTaskResultFileSummary(Channel channel, String taskId);
}
