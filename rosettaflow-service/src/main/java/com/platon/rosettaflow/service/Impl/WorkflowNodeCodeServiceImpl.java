package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.WorkflowNodeCodeMapper;
import com.platon.rosettaflow.mapper.domain.AlgorithmCode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;
import com.platon.rosettaflow.service.IAlgorithmCodeService;
import com.platon.rosettaflow.service.IWorkflowNodeCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 工作流代码服务实现类
 *
 * @author hudenian
 * @date 2021/8/18
 */
@Slf4j
@Service
public class WorkflowNodeCodeServiceImpl extends ServiceImpl<WorkflowNodeCodeMapper, WorkflowNodeCode> implements IWorkflowNodeCodeService {

    @Resource
    private IAlgorithmCodeService algorithmCodeService;

    @Override
    public WorkflowNodeCode getByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        wrapper.eq(WorkflowNodeCode::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public Long addByAlgorithmIdAndWorkflowNodeId(Long algorithmId, Long workflowNodeId) {
        AlgorithmCode algorithmCode = algorithmCodeService.getByAlgorithmId(algorithmId);
        WorkflowNodeCode workflowNodeCode = new WorkflowNodeCode();
        workflowNodeCode.setWorkflowNodeId(workflowNodeId);
        workflowNodeCode.setEditType(algorithmCode.getEditType());
        workflowNodeCode.setCalculateContractCode(algorithmCode.getCalculateContractCode());
        workflowNodeCode.setDataSplitContractCode(algorithmCode.getDataSplitContractCode());
        this.save(workflowNodeCode);
        return workflowNodeCode.getId();
    }

    @Override
    public void deleteByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeCode> delWrapper = Wrappers.lambdaQuery();
        delWrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        delWrapper.eq(WorkflowNodeCode::getStatus, StatusEnum.UN_VALID.getValue());
        this.remove(delWrapper);
    }

    @Override
    public void deleteLogicByWorkflowNodeId(Long workflowNodeId) {
        LambdaUpdateWrapper<WorkflowNodeCode> delWrapper = Wrappers.lambdaUpdate();
        delWrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        delWrapper.set(WorkflowNodeCode::getStatus, StatusEnum.UN_VALID.getValue());
        this.update(delWrapper);
    }
}
