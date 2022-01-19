package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.dto.WorkflowNodeDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TerminateTaskRequestDto;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowTemp;

import java.util.List;
import java.util.Map;

/**
 * 工作流服务
 *
 * @author hudenian
 * @date 2021/8/16
 */
public interface IWorkflowService extends IService<Workflow> {

    /**
     * 获取工作流分页列表
     *
     * @param projectId    项目id
     * @param workflowName 项目名称
     * @param current      当前页
     * @param size         条数
     * @return WorkflowDto
     */
    IPage<WorkflowDto> queryWorkFlowPageList(Long projectId, String workflowName, Long current, Long size);


    /**
     * 通过项目id查询工作流列表（不分页）
     *
     * @param projectIdList 项目id列表
     * @return 工作流列表
     */
    List<Workflow> queryListByProjectId(List<Long> projectIdList);


    /**
     * 获取工作流详情
     *
     * @param id 工作流id
     * @return 工作流详情
     */
    Workflow queryWorkflow(Long id);

    /**
     * 添加工作流
     *
     * @param workflow 工作流对象
     */
    void addWorkflow(Workflow workflow);

    /**
     * 编辑工作流
     *
     * @param id           工作流id
     * @param workflowName 工作流名称
     * @param workflowDesc 工作流描述
     */
    void editWorkflow(Long id, String workflowName, String workflowDesc);

    /**
     * 逻辑删除工作流
     *
     * @param id 工作流表id
     */
    void deleteWorkflow(Long id);

    /**
     * 获取运行日志
     *
     * @param workflowId 工作流id
     * @return 运行日志
     */
    List<TaskEventDto> getTaskEventList(Long workflowId);

    /**
     * 根据项目id及工作流模板添加工作流
     *
     * @param projectId    项目id
     * @param userId       用户id
     * @param workflowTemp 工作流模板
     * @param language 语言类型
     * @return 工作流id
     */
    Workflow addWorkflowByTemplate(Long projectId, Long userId, WorkflowTemp workflowTemp, String language);

    /**
     * 终止工作流
     *
     * @param workflowId 工作流id
     */
    void terminate(Long workflowId);

    /**
     * 获取工作流状态
     *
     * @param id 工作流id
     * @return List
     */
    Workflow getWorkflowStatusById(Long id);

    /**
     * 是否存在指定的作业名称
     * @param workflowName
     * @return
     */
    boolean isExistWorkflowName(String workflowName);


    /**
     * 查询工作流节点设置及状态
     *
     * @param workflowId
     * @param language
     * @return
     */
    Workflow queryWorkflowDetailAndStatus(Long workflowId, String language);

    /**
     * 查询工作流节点设置
     *
     * @param workflowId
     * @param version
     * @return
     */
    Workflow queryWorkflowDetail(Long workflowId, Integer version);

    /**
     * 保存工作流设置详情
     *
     * @param workflow
     */
    void saveWorkflowDetail(Workflow workflow);

    void saveWorkflowDetailAndStart(Workflow workflow);

    void clearWorkflowNode(Long workflowId);
}
