package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflow;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowRunStatus;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowTemp;

import java.util.List;

/**
 * 工作流服务
 *
 * @author hudenian
 * @date 2021/8/16
 */
public interface ZOldIWorkflowService extends IService<ZOldWorkflow> {

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
    List<ZOldWorkflow> queryListByProjectId(List<Long> projectIdList);


    /**
     * 获取工作流详情
     *
     * @param id 工作流id
     * @return 工作流详情
     */
    ZOldWorkflow queryWorkflow(Long id);

    /**
     * 添加工作流
     *
     * @param workflow 工作流对象
     */
    void addWorkflow(ZOldWorkflow workflow);

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
     * @param workflowId
     * @param workflowRunStatusId
     * @return
     */
    List<TaskEventDto> getTaskEventList(Long workflowId, Long workflowRunStatusId);

    /**
     * 根据项目id及工作流模板添加工作流
     *
     * @param projectId    项目id
     * @param userId       用户id
     * @param workflowTemp 工作流模板
     * @param language 语言类型
     * @return 工作流id
     */
    ZOldWorkflow addWorkflowByTemplate(Long projectId, Long userId, ZOldWorkflowTemp workflowTemp, String language);

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
    ZOldWorkflow getWorkflowStatusById(Long id);

    /**
     * 是否存在指定的作业名称
     * @param workflowName
     * @return
     */
    boolean isExistWorkflowName(Long projectId,String workflowName);


    /**
     * 查询工作流节点设置及状态
     *
     * @param workflowId
     * @param workflowRunStatusId
     * @param language
     * @return
     */
    ZOldWorkflow queryWorkflowDetailAndStatus(Long workflowId, Long workflowRunStatusId, String language);

    /**
     * 查询工作流节点设置
     *
     * @param workflowId
     * @param version
     * @return
     */
    ZOldWorkflow queryWorkflowDetail(Long workflowId, Integer version);

    /**
     * 保存工作流设置详情
     *
     * @param workflow
     */
    void saveWorkflowDetail(ZOldWorkflow workflow);

    void saveWorkflowDetailAndStart(ZOldWorkflow workflow);

    void clearWorkflowNode(Long workflowId);

    IPage<ZOldWorkflowRunStatus> runningRecordList(Long current, Long size, Long projectId, String workflowName);

    ZOldWorkflow getWorkflowStatusById(Long id, Long runningRecordId);
}
