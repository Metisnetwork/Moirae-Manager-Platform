package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowRunTaskResultMapper;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowRunTaskResult;
import com.moirae.rosettaflow.service.ZOldIWorkflowRunTaskResultService;
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
public class ZOldWorkflowRunTaskResultServiceImpl extends ServiceImpl<ZOldWorkflowRunTaskResultMapper, ZOldWorkflowRunTaskResult> implements ZOldIWorkflowRunTaskResultService {

    @Resource
    private OrgService organizationService;

    @Override
    public List<ZOldWorkflowRunTaskResult> queryByTaskId(String taskId) {
        List<ZOldWorkflowRunTaskResult> taskResultList = getByTaskId(taskId);

        if(taskResultList.size() > 0){
            Set<String> identityIdSet = taskResultList.stream().map(ZOldWorkflowRunTaskResult::getIdentityId).collect(Collectors.toSet());
            List<Org> organizationList = organizationService.getOrgListByIdentityIdList(identityIdSet);
            Map<String, String> organizationMap = organizationList.stream().collect(Collectors.toMap(Org::getIdentityId, Org::getNodeName));
            taskResultList.stream().forEach(item -> {item.setIdentityName(organizationMap.get(item.getIdentityId()));});
        }
        return taskResultList;
    }

    private List<ZOldWorkflowRunTaskResult> getByTaskId(String taskId){
        LambdaQueryWrapper<ZOldWorkflowRunTaskResult> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowRunTaskResult::getTaskId, taskId);
        return this.list(wrapper);
    }
}
