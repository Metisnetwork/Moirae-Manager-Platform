package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.manager.WorkflowTaskResourceManager;
import com.moirae.rosettaflow.mapper.WorkflowTaskResourceMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskResource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流任务资源表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskResourceManagerImpl extends ServiceImpl<WorkflowTaskResourceMapper, WorkflowTaskResource> implements WorkflowTaskResourceManager {

    @Override
    public void clearAndSave(List<Long> taskIdList, List<WorkflowTaskResource> workflowTaskResourceList) {
        removeByIds(taskIdList);
        saveBatch(workflowTaskResourceList);
    }

    @Override
    public Map<OldAndNewEnum, WorkflowTaskResource> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId) {
        Map<OldAndNewEnum, WorkflowTaskResource> pair = new HashMap<>();
        WorkflowTaskResource old = getById(oldWorkflowTaskId);
        if(old == null){
            pair.put(OldAndNewEnum.OLD, null);
            pair.put(OldAndNewEnum.NEW, null);
            return pair;
        }
        WorkflowTaskResource newObj = new WorkflowTaskResource();
        newObj.setWorkflowTaskId(newWorkflowTaskId);
        newObj.setCostBandwidth(old.getCostBandwidth());
        newObj.setRunTime(old.getRunTime());
        newObj.setCostMem(old.getCostMem());
        newObj.setCostGpu(old.getCostGpu());
        newObj.setCostCpu(old.getCostCpu());
        save(newObj);
        pair.put(OldAndNewEnum.OLD, old);
        pair.put(OldAndNewEnum.NEW, newObj);
        return pair;
    }

    @Override
    public WorkflowTaskResource deleteByWorkflowTaskId(Long workflowTaskId) {
        WorkflowTaskResource workflowTaskResource = getById(workflowTaskId);
        if(workflowTaskResource != null){
            removeById(workflowTaskResource.getWorkflowTaskId());
        }
        return workflowTaskResource;
    }
}
