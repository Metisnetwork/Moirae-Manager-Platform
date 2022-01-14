package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowNodeCodeMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeCode;
import com.moirae.rosettaflow.service.IWorkflowNodeCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public WorkflowNodeCode queryByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        return this.getOne(wrapper);
    }

    @Override
    public WorkflowNodeCode copyWorkflowNodeCode(Long newNodeId, Long oldNodeId) {
        WorkflowNodeCode oldNodeCode = this.queryByWorkflowNodeId(oldNodeId);
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
