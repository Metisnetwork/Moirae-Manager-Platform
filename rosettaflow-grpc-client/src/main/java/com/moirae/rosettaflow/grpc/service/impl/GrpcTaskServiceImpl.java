package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.client.TaskServiceClient;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.service.PublishTaskDeclareResponse;
import com.moirae.rosettaflow.grpc.task.req.dto.*;
import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
@Profile({"prod", "test", "local", "xty"})
public class GrpcTaskServiceImpl implements GrpcTaskService {

    @Resource
    private TaskServiceClient taskServiceClient;

    @Override
    public PublishTaskDeclareResponseDto syncPublishTask(TaskDto taskDto) {
        return taskServiceClient.syncPublishTask(taskDto);
    }

    @Override
    public void asyncPublishTask(TaskDto taskDto, Consumer<PublishTaskDeclareResponse> callback) {
        taskServiceClient.asyncPublishTask(taskDto, callback);
    }

    @Override
    public List<TaskDetailResponseDto> getTaskDetailList() {
        return taskServiceClient.getTaskDetailList();
    }

    @Override
    public List<TaskEventDto> getTaskEventList(String taskId) {
        return taskServiceClient.getTaskEventList(taskId);
    }

    @Override
    public List<TaskEventDto> getTaskEventListByTaskIds(String[] taskIds) {
        return taskServiceClient.getTaskEventListByTaskIds(taskIds);
    }

    @Override
    public TerminateTaskRespDto terminateTask(TerminateTaskRequestDto requestDto) {
        return taskServiceClient.terminateTask(requestDto);
    }
}
