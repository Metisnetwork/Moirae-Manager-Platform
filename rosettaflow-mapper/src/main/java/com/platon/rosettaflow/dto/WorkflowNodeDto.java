package com.platon.rosettaflow.dto;

import com.platon.rosettaflow.mapper.domain.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 工作流节点数据对象
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowNodeDto extends WorkflowNode {

    /**
     * 算法对象
     */
    private AlgorithmDto algorithmDto;

    /**
     * 工作流节点输入列表
     */
    private List<WorkflowNodeInput> workflowNodeInputList;

    /**
     * 工作流节点输出列表
     */
    private List<WorkflowNodeOutput> workflowNodeOutputList;
    /**
     * 工作流节点代码请求对象
     */
    private WorkflowNodeCode workflowNodeCode;
    /**
     * 工作流节点资源请求对象
     */
    private WorkflowNodeResource workflowNodeResource;
    /**
     * 工作流节点输入变量请求对象
     */
    private List<WorkflowNodeVariable> workflowNodeVariableList;
}
