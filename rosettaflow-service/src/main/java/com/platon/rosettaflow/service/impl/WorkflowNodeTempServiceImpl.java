package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.common.enums.WorkflowRunStatusEnum;
import com.platon.rosettaflow.mapper.WorkflowNodeTempMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeTemp;
import com.platon.rosettaflow.service.IWorkflowNodeTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流节点模板服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeTempServiceImpl extends ServiceImpl<WorkflowNodeTempMapper, WorkflowNodeTemp> implements IWorkflowNodeTempService {
    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    @Override
    public List<WorkflowNodeTemp> getByWorkflowTempId(Long workflowTempId) {
        LambdaQueryWrapper<WorkflowNodeTemp> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeTemp::getWorkflowTempId, workflowTempId);
        wrapper.eq(WorkflowNodeTemp::getStatus, StatusEnum.VALID.getValue());
        wrapper.orderByAsc(WorkflowNodeTemp::getId);
        return this.list(wrapper);
    }

    @Override
    public void addWorkflowNodeList(long workflowTemplateId, List<WorkflowNode> workflowNodeList) {
        List<WorkflowNodeTemp> workflowNodeTempList = new ArrayList<>();
        WorkflowNodeTemp workflowNodeTemp;
        for (WorkflowNode workflowNode : workflowNodeList) {
            workflowNodeTemp = new WorkflowNodeTemp();
            workflowNodeTemp.setWorkflowTempId(workflowTemplateId);
            workflowNodeTemp.setAlgorithmId(workflowNode.getAlgorithmId());
            workflowNodeTemp.setNodeName(workflowNode.getNodeName());
            workflowNodeTemp.setNodeStep(workflowNode.getNodeStep());
            workflowNodeTemp.setRunStatus(WorkflowRunStatusEnum.UN_RUN.getValue());
            workflowNodeTempList.add(workflowNodeTemp);
        }
        this.saveBatch(workflowNodeTempList);
    }
}
