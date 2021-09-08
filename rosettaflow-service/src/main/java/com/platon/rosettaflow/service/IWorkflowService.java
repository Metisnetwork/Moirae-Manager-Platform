package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.mapper.domain.Workflow;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * 工作流服务
 * @author hudenian
 * @date 2021/8/16
 */
public interface IWorkflowService extends IService<Workflow> {

    /**
     * 获取工作流分页列表
     * @param projectId 项目id
     * @param workflowName 项目名称
     * @param current 当前页
     * @param size 条数
     * @return WorkflowDto
     */
    IPage<WorkflowDto> queryWorkFlowPageList(Long projectId, String workflowName, Long current, Long size);

    /**
     * 获取工作流列表（不分页）
     * @param projectId 项目id
     * @return Workflow 工作流
     */
    List<Workflow> queryWorkFlowList(Long projectId);

    /**
     * 获取工作流详情
     * @param id 工作流id
     * @return 工作流详情
     */
    WorkflowDto queryWorkflowDetail(Long id);

    /**
     * 添加工作流
     * @param workflow 工作流对象
     * @return
     */
    void addWorkflow(Workflow workflow);

    /**
     * 编辑工作流
     * @param id 工作流id
     * @param workflowName 工作流名称
     * @param workflowDesc 工作流描述
     */
    void editWorkflow(Long id, String workflowName, String workflowDesc);

    /**
     * 删除工作流
     * @param id 工作流表id
     */
    void deleteWorkflow(Long id);

    /**
     * 复制工作流
     * @param originId 源工作流id
     * @param workflowName 工作流名称
     * @param workflowDesc 工作流描述
     */
    void copyWorkflow(Long originId, String workflowName, String workflowDesc);

    /**
     * 启动工作流
     * @param workflowDto 启动工作流请求对象
     */
    void start(WorkflowDto workflowDto);

}
