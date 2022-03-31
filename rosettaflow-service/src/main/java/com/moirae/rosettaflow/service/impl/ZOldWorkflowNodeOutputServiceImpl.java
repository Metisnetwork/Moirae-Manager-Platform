package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowNodeOutputMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeOutput;
import com.moirae.rosettaflow.service.ZOldIWorkflowNodeOutputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 作流节点输出服务实现类
 */
@Slf4j
@Service
public class ZOldWorkflowNodeOutputServiceImpl extends ServiceImpl<ZOldWorkflowNodeOutputMapper, ZOldWorkflowNodeOutput> implements ZOldIWorkflowNodeOutputService {
    @Override
    public List<ZOldWorkflowNodeOutput> getByWorkflowNodeId(Long workflowNodeId) {
        return baseMapper.getWorkflowNodeOutputAndOrgNameByNodeId(workflowNodeId);
    }

    @Override
    public String getOutputIdentityIdByTaskId(String taskId){
        return this.baseMapper.getOutputIdentityIdByTaskId(taskId);
    }

    @Override
    public void batchInsert(List<ZOldWorkflowNodeOutput> workflowNodeOutputList) {
        this.baseMapper.batchInsert(workflowNodeOutputList);
    }

    @Override
    public List<ZOldWorkflowNodeOutput> queryByWorkflowNodeId(Long id) {
        LambdaQueryWrapper<ZOldWorkflowNodeOutput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowNodeOutput::getWorkflowNodeId, id);
        return this.list(wrapper);
    }
}
