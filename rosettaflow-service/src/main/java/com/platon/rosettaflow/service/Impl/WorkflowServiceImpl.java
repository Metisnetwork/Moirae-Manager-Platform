package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.mapper.WorkflowMapper;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.service.IWorkflowNodeService;
import com.platon.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        //所以此处先执行第一个节点，后继节点在定时任务中，待第一个节点执行成功后再执行
        WorkflowNode workflowNode = workflowNodeService.getByWorkflowIdAndStep(workflow.getId(), workflowDto.getStartNode());

        //获取工作流节点输入信息

        //获取工作流节点输出信息

        //获取工作流节点自变量及因变量
    }
}
