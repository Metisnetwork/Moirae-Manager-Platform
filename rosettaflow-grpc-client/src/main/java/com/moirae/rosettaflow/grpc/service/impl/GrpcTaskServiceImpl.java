package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.client.TaskServiceClient;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.SelfMetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.service.PublishTaskDeclareResponse;
import com.moirae.rosettaflow.grpc.task.req.dto.*;
import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public PublishTaskDeclareResponseDto syncPublishTask(Channel channel, TaskDto taskDto) {
        return taskServiceClient.syncPublishTask(channel,taskDto);
    }

    @Override
    public void asyncPublishTask(TaskDto taskDto, Consumer<PublishTaskDeclareResponse> callback) {
        taskServiceClient.asyncPublishTask(taskDto, callback);
    }

    @Override
    public List<TaskDetailResponseDto> getAllTaskDetailList(Channel channel) {
        long latestSynced = 0;
        List<TaskDetailResponseDto> allList = new ArrayList<>();
        List<TaskDetailResponseDto> list;
        do {
            list = taskServiceClient.getTaskDetailList(channel,latestSynced);
            if(list.isEmpty()){
                break;
            }
            allList.addAll(list);
            latestSynced = list.get(list.size() - 1).getInformation().getUpdateAt();
        } while (list.size() == GrpcConstant.PAGE_SIZE);//如果小于pageSize说明是最后一批了
        return allList;
    }

    @Override
    public List<TaskDetailResponseDto> getTaskDetailList(Channel channel, Long latestSynced) {
        return taskServiceClient.getTaskDetailList(channel, latestSynced);
    }

    @Override
    public List<TaskEventDto> getTaskEventList(ManagedChannel channel, String taskId) {
        return taskServiceClient.getTaskEventList(channel,taskId);
    }

    @Override
    public List<TaskEventDto> getTaskEventListByTaskIds(ManagedChannel channel, String[] taskIds) {
        return taskServiceClient.getTaskEventListByTaskIds(channel,taskIds);
    }

    @Override
    public TerminateTaskRespDto terminateTask(Channel channel,TerminateTaskRequestDto requestDto) {
        return taskServiceClient.terminateTask(channel, requestDto);
    }
}
