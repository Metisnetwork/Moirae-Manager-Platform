package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.manager.WorkflowVersionManager;
import com.moirae.rosettaflow.mapper.WorkflowVersionMapper;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;
import com.moirae.rosettaflow.mapper.enums.WorkflowTaskRunStatusEnum;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作流不同版本设置表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowVersionManagerImpl extends ServiceImpl<WorkflowVersionMapper, WorkflowVersion> implements WorkflowVersionManager {

    @Override
    public IPage<WorkflowVersion> getWorkflowVersionList(Page<WorkflowVersion> page, Long workflowId) {
        return baseMapper.getWorkflowVersionList(page, workflowId);
    }

    @Override
    public WorkflowVersion create(Long workflowId, Long workflowVersionNumber, String workflowVersionName) {
        WorkflowVersion workflowVersion = new WorkflowVersion();
        workflowVersion.setWorkflowId(workflowId);
        workflowVersion.setWorkflowVersion(workflowVersionNumber);
        workflowVersion.setWorkflowVersionName(workflowVersionName);
        workflowVersion.setStatus(WorkflowTaskRunStatusEnum.RUN_NEED);

        try{
            if(save(workflowVersion)){
                return workflowVersion;
            }else {
                return null;
            }
        }catch (DuplicateKeyException e) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_VERSION_NAME_EXIST.getMsg(),e);
        }
    }

    @Override
    public List<WorkflowVersion> deleteByWorkflowId(Long workflowId) {
        LambdaQueryWrapper<WorkflowVersion> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowVersion::getWorkflowId, workflowId);
        List<WorkflowVersion> result = list(wrapper);
        if(result.size() > 0){
            remove(wrapper);
        }
        return result;
    }

    @Override
    public WorkflowVersion getById(Long workflowId, Long workflowVersion) {
        LambdaQueryWrapper<WorkflowVersion> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowVersion::getWorkflowId, workflowId);
        wrapper.eq(WorkflowVersion::getWorkflowVersion, workflowVersion);
        return getOne(wrapper);
    }

    @Override
    public List<WorkflowVersion> listByNameAndId(Long workflowId, String workflowVersionName) {
        LambdaQueryWrapper<WorkflowVersion> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowVersion::getWorkflowId, workflowId);
        wrapper.eq(WorkflowVersion::getWorkflowVersionName, workflowVersionName);
        return list(wrapper);
    }
}
