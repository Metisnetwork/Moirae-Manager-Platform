package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点代码服务
 */
public interface IWorkflowNodeCodeService extends IService<WorkflowNodeCode> {
    /**
     * 根据工作流节点id获取工作流节点代码
     *
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点节点代码
     */
    WorkflowNodeCode getByWorkflowNodeId(Long workflowNodeId);

    /**
     * 根据工作流节点id删除工作流节点代码
     *
     * @param workflowNodeId 工作流节点id
     */
    void deleteByWorkflowNodeId(Long workflowNodeId);

    /**
     * 根据算法id及工作流节点id添加工作流节点算法代码
     * @param algorithmId 算法id
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点代码表id
     */
    Long addByAlgorithmIdAndWorkflowNodeId(Long algorithmId, Long workflowNodeId);
}
