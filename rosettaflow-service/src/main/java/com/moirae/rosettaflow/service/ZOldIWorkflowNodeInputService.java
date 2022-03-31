package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeInput;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点输入服务接口
 */
public interface ZOldIWorkflowNodeInputService extends IService<ZOldWorkflowNodeInput> {

    /**
     * 根据工作流节点id获取工作流节点输入列表
     *
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点输入列表
     */
    List<ZOldWorkflowNodeInput> queryByWorkflowNodeId(Long workflowNodeId);

    /**
     * 批量保存节点输入
     *
     * @param workflowNodeInputList 节点输入列表
     */
    void batchInsert(List<ZOldWorkflowNodeInput> workflowNodeInputList);
}
