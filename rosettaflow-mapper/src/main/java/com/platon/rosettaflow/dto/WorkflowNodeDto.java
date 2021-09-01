package com.platon.rosettaflow.dto;

import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeInput;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeOutput;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/30
 * @description 工作流节点数据对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowNodeDto extends WorkflowNode {

    /**
     * 算法对象
     */
    private AlgorithmDto algorithmDto;

    /**
     * 环境
     */
    private WorkflowNodeResource workflowNodeResource;

    /**
     * 工作流节点输入列表
     */
    private List<WorkflowNodeInput> workflowNodeInputList;

    /**
     * 工作流节点输出列表
     */
    private List<WorkflowNodeOutput> workflowNodeOutputList;
}
