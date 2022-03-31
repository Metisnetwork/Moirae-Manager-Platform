package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * t_workflow_node_code
 *
 * @author admin
 */
public interface ZOldWorkflowNodeCodeMapper extends BaseMapper<ZOldWorkflowNodeCode> {
    /**
     * 批量保存节点代码
     *
     * @param workflowNodeCodeList 节点代码列表
     * @return 保存记录数
     */
    int batchInsert(@Param("workflowNodeCodeList") List<ZOldWorkflowNodeCode> workflowNodeCodeList);
}
