package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.service.PublishTaskDeclareResponse;
import com.moirae.rosettaflow.grpc.task.req.dto.*;
import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 任务处理mock服务
 */
@Slf4j
@Service
@Profile({"dev", "uat"})
public class GrpcTaskServiceMockImpl implements GrpcTaskService {

    @Override
    public PublishTaskDeclareResponseDto syncPublishTask(Channel channel, TaskDto taskDto) {
        PublishTaskDeclareResponseDto responseDto = new PublishTaskDeclareResponseDto();
        responseDto.setTaskId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        responseDto.setMsg("mocker 处理成功！");
        responseDto.setStatus(GrpcConstant.GRPC_SUCCESS_CODE);
        return responseDto;
    }

    @Override
    public void asyncPublishTask(TaskDto taskDto, Consumer<PublishTaskDeclareResponse> callback) {
        PublishTaskDeclareResponse publishTaskDeclareResponse = PublishTaskDeclareResponse.newBuilder()
                .setStatus(GrpcConstant.GRPC_SUCCESS_CODE)
                .setTaskId(UUID.randomUUID().toString().replace("-", "").toLowerCase())
                .setMsg("mocker 处理成功！")
                .build();
        callback.accept(publishTaskDeclareResponse);
    }

    @Override
    public List<TaskDetailResponseDto> getTaskDetailList(Channel channel) {
        return null;
    }

    @Override
    public List<TaskEventDto> getTaskEventList(ManagedChannel channel, String taskId) {
        List<TaskEventDto> taskEventShowDtoList = new ArrayList<>();
        TaskEventDto taskEventShowDto = new TaskEventDto();
        NodeIdentityDto nodeIdentityDto = new NodeIdentityDto();

        taskEventShowDto.setType("1");
        taskEventShowDto.setTaskId(taskId);

        nodeIdentityDto.setNodeName("nodeName");
        nodeIdentityDto.setNodeId("nodeId");
        nodeIdentityDto.setIdentityId("identityId");

        taskEventShowDto.setOwner(nodeIdentityDto);
        taskEventShowDto.setContent("处理成功");
        taskEventShowDto.setCreateAt(1877777777787L);
        taskEventShowDtoList.add(taskEventShowDto);
        return taskEventShowDtoList;
    }

    @Override
    public List<TaskEventDto> getTaskEventListByTaskIds(ManagedChannel channel, String[] taskIds) {
        List<TaskEventDto> taskEventShowDtoList = new ArrayList<>();
        TaskEventDto taskEventShowDto;
        NodeIdentityDto nodeIdentityDto;

        for (int i = 0; i < taskIds.length; i++) {
            taskEventShowDto = new TaskEventDto();
            taskEventShowDto.setType("1");
            taskEventShowDto.setTaskId(taskIds[i]);

            nodeIdentityDto = new NodeIdentityDto();
            nodeIdentityDto.setNodeName("nodeName" + i);
            nodeIdentityDto.setNodeId("nodeId" + i);
            nodeIdentityDto.setIdentityId("identityId" + i);

            taskEventShowDto.setOwner(nodeIdentityDto);
            taskEventShowDto.setContent("处理成功");
            taskEventShowDto.setCreateAt(1877777777787L);
            taskEventShowDtoList.add(taskEventShowDto);
        }
        return taskEventShowDtoList;
    }

    @Override
    public TerminateTaskRespDto terminateTask(Channel channel, TerminateTaskRequestDto requestDto) {
        TerminateTaskRespDto respDto = new TerminateTaskRespDto();
        respDto.setStatus(GrpcConstant.GRPC_SUCCESS_CODE);
        respDto.setMsg("处理成功");
        return respDto;
    }
}
