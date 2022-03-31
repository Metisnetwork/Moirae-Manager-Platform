package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowTempNode;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流节点模板表
 */
public interface ZOldIWorkflowTempNodeService extends IService<ZOldWorkflowTempNode> {

    /**
     * 根据工作流模板id获取工作流节点列表
     *
     * @param workflowTempId 工作流模板id
     * @return 工作流节点模板列表
     */
    List<ZOldWorkflowTempNode> getByWorkflowTempId(Long workflowTempId);

}
