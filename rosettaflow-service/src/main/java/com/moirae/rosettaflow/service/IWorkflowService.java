package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.WorkflowDto;
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
     * 查询工作流列表，通过工作流id集合
     *
     * @param idList 工作流id列表
     * @return 工作流列表
     */
    List<Workflow> queryListById(List<Long> idList);

    /**
     * 通过项目id查询工作流列表（不分页）
     *
     * @param projectIdList 项目id列表
     * @return 工作流列表
     */
    List<Workflow> queryListByProjectId(List<Long> projectIdList);

    /**
     * 获取运行中的工作流列表（不分页）
     *
     * @param projectId 项目id
     * @return 工作流列表
     */
    List<Workflow> queryWorkFlowByProjectId(Long projectId);

    /**
     * 获取工作流详情
     *
     * @param id 工作流id
     * @return 工作流详情
     */
    Workflow queryWorkflowDetail(Long id);

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
     * 批量逻辑删除工作流
     *
     * @param ids 多个工作流表id
     */
    void deleteWorkflowBatch(String ids);

    /**
     * 物理删除当前工作流所有节点数据
     *
     * @param id 工作流表id
     */
    void deleteWorkflowAllNodeData(Long id);

    /**
     * 复制工作流
     *
     * @param originId     源工作流id
     * @param workflowName 工作流名称
     * @param workflowDesc 工作流描述
     */
    void copyWorkflow(Long originId, String workflowName, String workflowDesc);

    /**
     * 启动工作流
     *
     * @param workflowDto 启动工作流请求对象
     */
    void start(WorkflowDto workflowDto);

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
     * @return 工作流id
     */
    Long addWorkflowByTemplate(Long projectId, Long userId, WorkflowTemp workflowTemp);

    /**
     * 终止工作流
     *
     * @param workflowId 工作流id
     */
    void terminate(Long workflowId);

    /**
     * 更新工作流运行状态
     *
     * @param workflowId 工作流id
     * @param runStatus  运行状态
     */
    void updateRunStatus(Long workflowId, Byte runStatus);

    /**
     * 获取工作流状态
     *
     * @param id 工作流id
     * @return List
     */
    Map<String, Object> getWorkflowStatusById(Long id);

    /**
     * 组装发送任务对象
     *
     * @param workflowDto 工作流节点参数信息
     * @return 发送任务对象
     */
    TaskDto assemblyTaskDto(WorkflowDto workflowDto);

    /**
     * 更新工作流运行状态
     *
     * @param ids       工作流id
     * @param runStatus 运行状态
     */
    void updateRunStatus(Object[] ids, Byte runStatus);

    /**
     * 组装发送终止任务请求对象
     *
     * @param workflow 工作流信息
     * @param taskId   任务节点taskId
     * @return 发送终止任务请求对象
     */
    TerminateTaskRequestDto assemblyTerminateTaskRequestDto(Workflow workflow, String taskId);

}
