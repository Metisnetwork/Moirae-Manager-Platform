package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.mapper.domain.Workflow;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 工作流服务
 */
public interface IWorkflowService extends IService<Workflow> {

    /**
     * 启动工作流
     *
     * @param workflowDto 启动工作流请求对象
     */
    void start(WorkflowDto workflowDto);
}
