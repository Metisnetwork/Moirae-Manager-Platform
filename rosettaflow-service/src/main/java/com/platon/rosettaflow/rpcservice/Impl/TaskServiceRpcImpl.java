package com.platon.rosettaflow.rpcservice.Impl;

import com.platon.rosettaflow.common.enums.WorkflowRunStatusEnum;
import com.platon.rosettaflow.grpc.client.TaskServiceClient;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.task.dto.PublishTaskDeclareResponseDto;
import com.platon.rosettaflow.grpc.task.dto.TaskDto;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.service.IJobService;
import com.platon.rosettaflow.rpcservice.ITaskServiceRpc;
import com.platon.rosettaflow.service.IWorkflowNodeService;
import com.platon.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 功能描述
 */
@Slf4j
@Service
public class TaskServiceRpcImpl implements ITaskServiceRpc {

    @Resource
    private IJobService jobService;

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private IWorkflowNodeService workflowNodeService;

    @Resource
    private TaskServiceClient taskServiceClient;

    @Override
    public PublishTaskDeclareResponseDto syncPublishTask(TaskDto taskDto) {
        return null;
    }

    @Override
    public void asyncPublishTask(TaskDto taskDto) {
        taskServiceClient.asyncPublishTask(taskDto,
                publishTaskDeclareResponse -> {
                    //更新工作流节点
                    WorkflowNode workflowNode = workflowNodeService.getById(taskDto.getWorkFlowNodeId());
                    Workflow workflow;
                    if (publishTaskDeclareResponse.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
                        //处理成功
                        workflowNode.setTaskId(publishTaskDeclareResponse.getTaskId());
                        workflowNode.setRunStatus(WorkflowRunStatusEnum.RUN_SUCCESS.getValue());

                        //更新整个工作流信息:如果是最后一个节点，整个工作流更新成处理成功，否则更新成处理中
                        workflow = workflowService.getById(workflowNode.getWorkflowId());
                        if (workflow.getNodeNumber() == workflowNode.getNodeStep().intValue()) {
                            workflow.setRunStatus(WorkflowRunStatusEnum.RUN_SUCCESS.getValue());
                        } else {
                            workflow.setRunStatus(WorkflowRunStatusEnum.RUNNING.getValue());
                        }
                    } else {
                        //处理失败
                        workflowNode.setTaskId(publishTaskDeclareResponse.getTaskId() != null ? publishTaskDeclareResponse.getTaskId() : "");
                        workflowNode.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());

                        //更新整个工作流信息:处理失败
                        workflow = workflowService.getById(workflowNode.getWorkflowId());
                        workflow.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
                    }
                    workflowNode.setRunMsg(publishTaskDeclareResponse.getMsg());
                    workflowNodeService.updateById(workflowNode);

                    workflowService.updateById(workflow);
                });
    }
}
