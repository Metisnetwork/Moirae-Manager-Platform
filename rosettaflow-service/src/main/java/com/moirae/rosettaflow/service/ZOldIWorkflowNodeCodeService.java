package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeCode;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点代码服务
 */
public interface ZOldIWorkflowNodeCodeService extends IService<ZOldWorkflowNodeCode> {
    /**
     * 根据工作流节点id获取工作流节点代码
     *
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点节点代码
     */
    ZOldWorkflowNodeCode queryByWorkflowNodeId(Long workflowNodeId);


    /**
     * 批量保存节点代码列表
     *
     * @param workflowNodeCodeList 节点代码列表
     */
    void batchInsert(List<ZOldWorkflowNodeCode> workflowNodeCodeList);
}
