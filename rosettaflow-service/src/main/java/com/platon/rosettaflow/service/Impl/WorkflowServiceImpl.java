package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.mapper.WorkflowMapper;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021/8/16
 * @description 工作流服务实现类
 */
@Slf4j
@Service
public class WorkflowServiceImpl extends ServiceImpl<WorkflowMapper, Workflow> implements IWorkflowService {
    @Override
    public void start(WorkflowDto workflowDto) {
        Workflow workflow = this.getById(workflowDto.getId());
        if(workflow == null){
            log.error("workflow not exit by id:{}",workflowDto.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }
        if (workflow.getNodeNumber() < workflowDto.getEndNode()) {
            log.error("endNode is:{} can not more than workflow nodeNumber:{}", workflowDto.getEndNode(), workflow.getNodeNumber());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_END_NODE_OVERFLOW.getMsg());
        }
    }
}
