package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.common.enums.OldAndNewEnum;
import com.datum.platform.mapper.domain.Algorithm;
import com.datum.platform.mapper.domain.WorkflowTaskResource;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流任务资源表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowTaskResourceManager extends IService<WorkflowTaskResource> {

    void clearAndSave(List<Long> taskIdList, List<WorkflowTaskResource> workflowTaskResourceList);

    Map<OldAndNewEnum, WorkflowTaskResource> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId);

    WorkflowTaskResource deleteByWorkflowTaskId(Long workflowTaskId);

    WorkflowTaskResource create(Long workflowTaskId, Algorithm algorithm);
}
