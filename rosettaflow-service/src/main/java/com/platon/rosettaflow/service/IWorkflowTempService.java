package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowTemp;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流模板服务
 */
public interface IWorkflowTempService extends IService<WorkflowTemp> {
    /**
     * 清空项目模板表
     */
    void truncate();
}
