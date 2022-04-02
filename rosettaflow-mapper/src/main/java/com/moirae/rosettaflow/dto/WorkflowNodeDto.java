package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.*;
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
public class WorkflowNodeDto extends ZOldWorkflowNode {

    /**
     * 工作流节点输入列表
     */
    private List<ZOldWorkflowNodeInput> workflowNodeInputList;

    /**
     * 工作流节点输出列表
     */
    private List<ZOldWorkflowNodeOutput> workflowNodeOutputList;
    /**
     * 工作流节点代码请求对象
     */
    private ZOldWorkflowNodeCode workflowNodeCode;
    /**
     * 工作流节点资源请求对象
     */
    private ZOldWorkflowNodeResource workflowNodeResource;
    /**
     * 工作流节点输入变量请求对象
     */
    private List<ZOldWorkflowNodeVariable> workflowNodeVariableList;
}
