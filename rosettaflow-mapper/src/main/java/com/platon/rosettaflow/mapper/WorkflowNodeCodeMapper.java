package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * t_workflow_node_code
 *
 * @author admin
 */
public interface WorkflowNodeCodeMapper extends BaseMapper<WorkflowNodeCode> {
    /**
     * 批量保存节点代码
     *
     * @param workflowNodeCodeList 节点代码列表
     * @return 保存记录数
     */
    int batchInsert(@Param("workflowNodeCodeList") List<WorkflowNodeCode> workflowNodeCodeList);
}