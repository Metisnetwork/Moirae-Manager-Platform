package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowRunTaskResultMapper;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskResult;
import com.moirae.rosettaflow.service.IWorkflowRunTaskResultService;
import com.moirae.rosettaflow.service.OrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkflowRunTaskResultServiceImpl extends ServiceImpl<WorkflowRunTaskResultMapper, WorkflowRunTaskResult> implements IWorkflowRunTaskResultService {

    @Resource
    private OrgService organizationService;

    @Override
    public List<WorkflowRunTaskResult> queryByTaskId(String taskId) {
        List<WorkflowRunTaskResult> taskResultList = getByTaskId(taskId);

        if(taskResultList.size() > 0){
            Set<String> identityIdSet = taskResultList.stream().map(WorkflowRunTaskResult::getIdentityId).collect(Collectors.toSet());
            List<Org> organizationList = organizationService.getOrgListByIdentityIdList(identityIdSet);
            Map<String, String> organizationMap = organizationList.stream().collect(Collectors.toMap(Org::getIdentityId, Org::getNodeName));
            taskResultList.stream().forEach(item -> {item.setIdentityName(organizationMap.get(item.getIdentityId()));});
        }
        return taskResultList;
    }

    private List<WorkflowRunTaskResult> getByTaskId(String taskId){
        LambdaQueryWrapper<WorkflowRunTaskResult> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunTaskResult::getTaskId, taskId);
        return this.list(wrapper);
    }
}
