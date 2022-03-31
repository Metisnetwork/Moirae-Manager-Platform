package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface ZOldWorkflowNodeResourceMapper extends BaseMapper<ZOldWorkflowNodeResource> {
    /**
     * 批量保存节点资源
     *
     * @param workflowNodeResourceList 节点资源列表
     * @return 保存记录数
     */
    int batchInsert(@Param("workflowNodeResourceList") List<ZOldWorkflowNodeResource> workflowNodeResourceList);
}
