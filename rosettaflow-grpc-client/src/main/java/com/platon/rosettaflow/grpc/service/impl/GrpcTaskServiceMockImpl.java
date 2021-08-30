package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.service.GrpcTaskService;
import com.platon.rosettaflow.grpc.service.PublishTaskDeclareResponse;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.platon.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 任务处理mock服务
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
    public List<TaskEventDto> getTaskEventList(String taskId) {
        List<TaskEventDto> taskEventShowDtoList = new ArrayList<>();
        TaskEventDto taskEventShowDto = new TaskEventDto();
        NodeIdentityDto nodeIdentityDto = new NodeIdentityDto();

        taskEventShowDto.setType("1");
        taskEventShowDto.setTaskId(taskId);

        nodeIdentityDto.setName("name");
        nodeIdentityDto.setNodeId("nodeId");
        nodeIdentityDto.setIdentityId("identityId");

        taskEventShowDto.setOwner(nodeIdentityDto);
        taskEventShowDto.setContent("处理成功");
        taskEventShowDto.setCreateAt(1877777777787L);
        taskEventShowDtoList.add(taskEventShowDto);
        return taskEventShowDtoList;
    }

    @Override
    public List<TaskEventDto> getTaskEventListByTaskIds(String[] taskIds) {
        List<TaskEventDto> taskEventShowDtoList = new ArrayList<>();
        TaskEventDto taskEventShowDto;
        NodeIdentityDto nodeIdentityDto;

        for (int i = 0; i < taskIds.length; i++) {
            taskEventShowDto = new TaskEventDto();
            taskEventShowDto.setType("1");
            taskEventShowDto.setTaskId(taskIds[i]);

            nodeIdentityDto = new NodeIdentityDto();
            nodeIdentityDto.setName("name" + i);
            nodeIdentityDto.setNodeId("nodeId" + i);
            nodeIdentityDto.setIdentityId("identityId" + i);

            taskEventShowDto.setOwner(nodeIdentityDto);
            taskEventShowDto.setContent("处理成功");
            taskEventShowDto.setCreateAt(1877777777787L);
            taskEventShowDtoList.add(taskEventShowDto);
        }
        return taskEventShowDtoList;
    }
}
