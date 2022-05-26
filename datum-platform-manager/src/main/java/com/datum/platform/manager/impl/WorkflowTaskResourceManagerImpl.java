package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.common.enums.OldAndNewEnum;
import com.datum.platform.manager.WorkflowTaskResourceManager;
import com.datum.platform.mapper.WorkflowTaskResourceMapper;
import com.datum.platform.mapper.domain.Algorithm;
import com.datum.platform.mapper.domain.WorkflowTaskResource;
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

    @Override
    public WorkflowTaskResource create(Long workflowTaskId, Algorithm algorithm) {
        WorkflowTaskResource workflowTaskResource = new WorkflowTaskResource();
        workflowTaskResource.setWorkflowTaskId(workflowTaskId);
        workflowTaskResource.setCostCpu(algorithm.getCostCpu());
        workflowTaskResource.setCostGpu(algorithm.getCostGpu());
        workflowTaskResource.setCostMem(algorithm.getCostMem());
        workflowTaskResource.setCostBandwidth(algorithm.getCostBandwidth());
        workflowTaskResource.setRunTime(algorithm.getRunTime());
        if(save(workflowTaskResource)){
            return workflowTaskResource;
        }
        return null;
    }
}
