package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.WorkflowNodeCodeMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeInput;
import com.platon.rosettaflow.service.IWorkflowNodeCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 工作流代码服务实现类
 *
 * @author hudenian
 * @date 2021/8/18
 */
@Slf4j
@Service
public class WorkflowNodeCodeServiceImpl extends ServiceImpl<WorkflowNodeCodeMapper, WorkflowNodeCode> implements IWorkflowNodeCodeService {

    @Override
    public WorkflowNodeCode getByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        wrapper.eq(WorkflowNodeCode::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public void deleteByWorkflowNodeId(List<Long> workflowNodeIdList) {
        LambdaQueryWrapper<WorkflowNodeCode> delWrapper = Wrappers.lambdaQuery();
        delWrapper.in(WorkflowNodeCode::getWorkflowNodeId, workflowNodeIdList);
        this.remove(delWrapper);
    }

    @Override
    public void deleteLogicByWorkflowNodeId(Long workflowNodeId) {
        LambdaUpdateWrapper<WorkflowNodeCode> delWrapper = Wrappers.lambdaUpdate();
        delWrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        delWrapper.set(WorkflowNodeCode::getStatus, StatusEnum.UN_VALID.getValue());
        this.update(delWrapper);
    }

    @Override
    public WorkflowNodeCode copyWorkflowNodeCode(Long newNodeId, Long oldNodeId) {
        WorkflowNodeCode oldNodeCode = this.getByWorkflowNodeId(oldNodeId);
        if (Objects.isNull(oldNodeCode)) {
            return null;
        }
        WorkflowNodeCode newNodeCode = new WorkflowNodeCode();
        newNodeCode.setWorkflowNodeId(newNodeId);
        newNodeCode.setEditType(oldNodeCode.getEditType());
        newNodeCode.setCalculateContractCode(oldNodeCode.getCalculateContractCode());
        newNodeCode.setDataSplitContractCode(oldNodeCode.getDataSplitContractCode());
        return newNodeCode;
    }

    @Override
    public void batchInsert(List<WorkflowNodeCode> workflowNodeCodeList) {
        this.baseMapper.batchInsert(workflowNodeCodeList);
    }
}
