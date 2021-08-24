package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.service.GrpcTaskService;
import com.platon.rosettaflow.grpc.service.PublishTaskDeclareResponse;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskEventShowDto;
import com.platon.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
@Profile({"dev", "local"})
public class GrpcTaskServiceMockImpl implements GrpcTaskService {

    @Override
    public PublishTaskDeclareResponseDto syncPublishTask(TaskDto taskDto) {
        return null;
    }

    @Override
    public void asyncPublishTask(TaskDto taskDto, Consumer<PublishTaskDeclareResponse> callback) {

    }

    @Override
    public List<TaskDetailResponseDto> getTaskDetailList() {
        return null;
    }

    @Override
    public List<TaskEventShowDto> getTaskEventList(String taskId) {
        return null;
    }

    @Override
    public List<TaskEventShowDto> getTaskEventListByTaskIds(String[] taskIds) {
        return null;
    }
}
