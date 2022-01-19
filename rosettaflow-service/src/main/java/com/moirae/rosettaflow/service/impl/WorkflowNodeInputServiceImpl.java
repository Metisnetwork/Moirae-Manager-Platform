package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.dto.NodeMetaDataDto;
import com.moirae.rosettaflow.mapper.WorkflowNodeInputMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeInput;
import com.moirae.rosettaflow.service.IWorkflowNodeInputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点输入服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeInputServiceImpl extends ServiceImpl<WorkflowNodeInputMapper, WorkflowNodeInput> implements IWorkflowNodeInputService {

    @Override
    public List<WorkflowNodeInput> queryByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeInput::getWorkflowNodeId, workflowNodeId);
        wrapper.orderByAsc(WorkflowNodeInput::getPartyId);
        return this.list(wrapper);
    }

    @Override
    public void batchInsert(List<WorkflowNodeInput> workflowNodeInputList) {
        this.baseMapper.batchInsert(workflowNodeInputList);
    }
}
