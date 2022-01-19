package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowTemp;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流模板服务
 */
public interface IWorkflowTempService extends IService<WorkflowTemp> {

    /**
     * 查询工作流模板
     * @param projectTempId 项目模板id
     * @return 工作流模板
     */
    WorkflowTemp getWorkflowTemplate(long projectTempId);
}
