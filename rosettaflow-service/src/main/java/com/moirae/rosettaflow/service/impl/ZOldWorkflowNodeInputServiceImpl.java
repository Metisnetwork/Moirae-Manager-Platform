package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowNodeInputMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeInput;
import com.moirae.rosettaflow.service.ZOldIWorkflowNodeInputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点输入服务实现类
 */
@Slf4j
@Service
public class ZOldWorkflowNodeInputServiceImpl extends ServiceImpl<ZOldWorkflowNodeInputMapper, ZOldWorkflowNodeInput> implements ZOldIWorkflowNodeInputService {

    @Override
    public List<ZOldWorkflowNodeInput> queryByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<ZOldWorkflowNodeInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowNodeInput::getWorkflowNodeId, workflowNodeId);
        wrapper.orderByAsc(ZOldWorkflowNodeInput::getPartyId);
        return this.list(wrapper);
    }

    @Override
    public void batchInsert(List<ZOldWorkflowNodeInput> workflowNodeInputList) {
        this.baseMapper.batchInsert(workflowNodeInputList);
    }
}
