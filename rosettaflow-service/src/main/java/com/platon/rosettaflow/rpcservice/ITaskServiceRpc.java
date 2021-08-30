package com.platon.rosettaflow.rpcservice;

import com.platon.rosettaflow.grpc.task.req.dto.TaskDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.platon.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;

import java.util.List;

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

    /**
     * 查看某个任务id的事件列表
     *
     * @param taskId 任务id
     * @return 事件列表
     */
    List<TaskEventDto> getTaskEventList(String taskId);

    /**
     * 查看某个任务的全部事件列表通过批量的任务ID
     *
     * @param taskIds 任务id数组
     * @return 事件列表
     */
    public List<TaskEventDto> getTaskEventListByTaskIds(String[] taskIds);
}
