package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.grpc.task.dto.TaskDto;
import com.platon.rosettaflow.mapper.WorkflowMapper;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.rpcservice.ITaskServiceRpc;
import com.platon.rosettaflow.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 工作流服务实现类
 */
@Slf4j
@Service
public class WorkflowServiceImpl extends ServiceImpl<WorkflowMapper, Workflow> implements IWorkflowService {

    @Resource
    private IWorkflowNodeService workflowNodeService;

    @Resource
    private IWorkflowNodeCodeService workflowNodeCodeService;

    @Resource
    private IWorkflowNodeInputService workflowNodeInputService;

    @Resource
    private IWorkflowNodeOutputService workflowNodeOutputService;

    @Resource
    private IWorkflowNodeResourceService workflowNodeResourceService;

    @Resource
    private IWorkflowNodeVariableService workflowNodeVariableService;

    @Resource
    private ITaskServiceRpc taskServiceRpc;

    @Override
    public void start(WorkflowDto workflowDto) {
        Workflow workflow = this.getById(workflowDto.getId());
        if (workflow == null) {
            log.error("workflow not found by id:{}", workflowDto.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }
        if (workflow.getNodeNumber() < workflowDto.getEndNode()) {
            log.error("endNode is:{} can not more than workflow nodeNumber:{}", workflowDto.getEndNode(), workflow.getNodeNumber());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_END_NODE_OVERFLOW.getMsg());
        }

        //TODO 当有多个任务节点时，需要将第一个节点执行成功后，并获取执行结果数据做为下个节点入参，才可以继续执行后面节点
        //TODO 所以此处先执行第一个节点，后继节点在定时任务中，待第一个节点执行成功后再执行
        TaskDto taskDto = assemblyTaskDto(workflow.getId(), workflowDto.getStartNode());
        taskServiceRpc.asyncPublishTask(taskDto);
    }

    /**
     * 组装发布任务请求对象
     *
     * @param workFlowId  工作流id
     * @param currentNode 工作流节点序号
     * @return 发布任务请求对象
     */
    public TaskDto assemblyTaskDto(Long workFlowId, Integer currentNode) {
        WorkflowNode workflowNode = workflowNodeService.getByWorkflowIdAndStep(workFlowId, currentNode);

        //获取工作流代码输入信息
        WorkflowNodeCode workflowNodeCode = workflowNodeCodeService.getByWorkflowNodeId(workflowNode.getId());
        if(workflowNodeCode == null){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_CODE_NOT_EXIST.getMsg());
        }

        //获取工作流节点输入信息
        List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.getByWorkflowNodeId(workflowNode.getId());

        //获取工作流节点输出信息
        List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputService.getByWorkflowNodeId(workflowNode.getId());

        //获取工作流节点自变量及因变量
        List<WorkflowNodeVariable> workflowNodeVariableList = workflowNodeVariableService.getByWorkflowNodeId(workflowNode.getId());

        //工作流节点资源表
        WorkflowNodeResource workflowNodeResource = workflowNodeResourceService.getByWorkflowNodeId(workflowNode.getId());

        TaskDto taskDto = new TaskDto();
        //TODO 拼装请求参数

        return taskDto;

    }
}
