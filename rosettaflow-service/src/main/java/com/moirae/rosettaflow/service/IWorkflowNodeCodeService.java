package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeCode;

import java.util.List;

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
     * 物理批量删除工作流节点算法代码，根据节点id
     * @param workflowNodeIdList 工作流节点id列表
     */
    void deleteByWorkflowNodeId(List<Long> workflowNodeIdList);

    /**
     * 逻辑删除工作流节点代码, 根据工作流节点id
     *
     * @param workflowNodeId 工作流节点id
     */
    void deleteLogicByWorkflowNodeId(Long workflowNodeId);

    /**
     * 复制工作流节点算法代码
     *
     * @param newNodeId 新节点id
     * @param oldNodeId 旧节点id
     * @return 节点代码
     */
    WorkflowNodeCode copyWorkflowNodeCode(Long newNodeId, Long oldNodeId);

    /**
     * 批量保存节点代码列表
     *
     * @param workflowNodeCodeList 节点代码列表
     */
    void batchInsert(List<WorkflowNodeCode> workflowNodeCodeList);
}
