package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeTemp;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流节点模板表
 */
public interface IWorkflowNodeTempService extends IService<WorkflowNodeTemp> {
    /**
     * 清空工作流节点模板表
     */
    void truncate();
}
