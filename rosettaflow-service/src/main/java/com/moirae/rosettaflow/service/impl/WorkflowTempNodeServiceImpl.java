package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowTempNodeMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowTempNode;
import com.moirae.rosettaflow.service.IWorkflowTempNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流节点模板服务实现类
 */
@Slf4j
@Service
public class WorkflowTempNodeServiceImpl extends ServiceImpl<WorkflowTempNodeMapper, WorkflowTempNode> implements IWorkflowTempNodeService {

    @Override
    public List<WorkflowTempNode> getByWorkflowTempId(Long workflowTempId) {
        LambdaQueryWrapper<WorkflowTempNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTempNode::getWorkflowTempId, workflowTempId);
        wrapper.orderByAsc(WorkflowTempNode::getId);
        return this.list(wrapper);
    }
}
