package com.platon.rosettaflow.grpc.service;

import com.platon.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskEventShowDto;
import com.platon.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description grpc任务处理服务
 */
public interface GrpcTaskService {
    /**
     * 发布一个任务,同步等待结果
     *
     * @param taskDto 任务信息
     * @return 任务处理结果
     */
    PublishTaskDeclareResponseDto syncPublishTask(TaskDto taskDto);

    /**
     * 发布一个任务,异步等待结果
     *
     * @param taskDto  任务信息
     * @param callback 回调函数
     */
    void asyncPublishTask(TaskDto taskDto, Consumer<PublishTaskDeclareResponse> callback);

    /**
     * 查看全部任务详情列表
     *
     * @return 全部任务详情列表
     */
    List<TaskDetailResponseDto> getTaskDetailList();

    /**
     * 查看某个任务的全部事件列表通过单个任务ID
     *
     * @param taskId 任务id
     * @return 事件详情列表
     */
    List<TaskEventShowDto> getTaskEventList(String taskId);

    /**
     * 查看某个任务的全部事件列表通过批量的任务ID
     *
     * @param taskIds 任务id列表
     * @return 任务事件列表
     */
    List<TaskEventShowDto> getTaskEventListByTaskIds(String[] taskIds);
}
