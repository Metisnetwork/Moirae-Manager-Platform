package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeInput;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface ZOldWorkflowNodeInputMapper extends BaseMapper<ZOldWorkflowNodeInput> {

    /**
     * 批量保存节点输入
     *
     * @param workflowNodeInputList 节点输入列表
     * @return 保存记录数
     */
    int batchInsert(@Param("workflowNodeInputList") List<ZOldWorkflowNodeInput> workflowNodeInputList);

}
