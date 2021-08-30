package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 获取工作流列表
     *
     * @param workflowDto 获取工作流列表请求对象
     * @param current     当前页
     * @param size        每页大小
     * @return 工作流列表
     */
    IPage<WorkflowDto> list(WorkflowDto workflowDto, Long current, Long size);

    /**
     * 添加工作流
     *
     * @param workflowDto 添加工作流请求对象
     */
    void add(WorkflowDto workflowDto);

    /**
     * 启动工作流
     *
     * @param workflowDto 启动工作流请求对象
     */
    void start(WorkflowDto workflowDto);

    /**
     * 编辑工作流
     *
     * @param workflowDto 编辑工作流请求对象
     */
    void edit(WorkflowDto workflowDto);

    /**
     * 复制工作流
     *
     * @param workflowDto 创建工作流请求对象
     */
    void copy(WorkflowDto workflowDto);

    /**
     * 根据工作流名称获取工作流
     *
     * @param name 工作流名称
     * @return 工作流
     */
    Workflow getByWorkflowName(String name);

    /**
     * 删除工作流
     *
     * @param id 工作流表id
     */
    void delete(Long id);

    /**
     * 获取工作流详情
     * @param id 工作流id
     * @return 工作流详情
     */
    WorkflowDto detail(Long id);
}
