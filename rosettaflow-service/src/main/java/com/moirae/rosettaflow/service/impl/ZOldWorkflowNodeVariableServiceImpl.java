package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowNodeVariableMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeVariable;
import com.moirae.rosettaflow.service.ZOldIWorkflowNodeVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工作流节点变量服务实现类
 * @author hudenian
 * @date 2021/8/18
 */
@Slf4j
@Service
public class ZOldWorkflowNodeVariableServiceImpl extends ServiceImpl<ZOldWorkflowNodeVariableMapper, ZOldWorkflowNodeVariable> implements ZOldIWorkflowNodeVariableService {
    @Override
    public List<ZOldWorkflowNodeVariable> queryByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<ZOldWorkflowNodeVariable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowNodeVariable::getWorkflowNodeId, workflowNodeId);
        return this.list(wrapper);
    }

    @Override
    public void batchInsert(List<ZOldWorkflowNodeVariable> workflowNodeVariableList) {
        this.baseMapper.batchInsert(workflowNodeVariableList);
    }
}
