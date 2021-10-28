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
     * 清空项目模板表
     */
    void truncate();

    /**
     * 根据项目id及工作流信息创建 项目工作流模板
     *
     * @param projectTemplateId 项目模板id
     * @param workflow          工作流
     * @return 工作流模板id
     */
    Long addWorkflowTemplate(long projectTemplateId, Workflow workflow);

    /**
     * 查询工作流模板
     * @param projectTempId 项目模板id
     * @return 工作流模板
     */
    WorkflowTemp getWorkflowTemplate(long projectTempId);
}
