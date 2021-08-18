package com.platon.rosettaflow.rpcservice;

import com.platon.rosettaflow.grpc.task.dto.PublishTaskDeclareResponseDto;
import com.platon.rosettaflow.grpc.task.dto.TaskDto;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 功能描述
 */
public interface ITaskServiceRpc {

    /**
     * 同步发送任务服务
     *
     * @param taskDto 任务处理类
     * @return 处理结果
     */
    PublishTaskDeclareResponseDto syncPublishTask(TaskDto taskDto);

    /**
     * 异步发送任务服务,单个工作流的启动
     *
     * @param taskDto 任务处理类
     */
    void asyncPublishTask(TaskDto taskDto);
}
