package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.mapper.domain.WorkflowTaskResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
}
