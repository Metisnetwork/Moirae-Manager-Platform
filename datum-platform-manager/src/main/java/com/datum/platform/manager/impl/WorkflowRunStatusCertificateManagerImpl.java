package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.WorkflowRunStatusCertificateManager;
import com.datum.platform.mapper.WorkflowRunStatusCertificateMapper;
import com.datum.platform.mapper.domain.WorkflowRunStatusCertificate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作流任务运行状态 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowRunStatusCertificateManagerImpl extends ServiceImpl<WorkflowRunStatusCertificateMapper, WorkflowRunStatusCertificate> implements WorkflowRunStatusCertificateManager {

    @Override
    public List<WorkflowRunStatusCertificate> listByWorkflowRunId(Long workflowRunId) {
        LambdaQueryWrapper<WorkflowRunStatusCertificate> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatusCertificate::getWorkflowRunId, workflowRunId);
        return list(wrapper);
    }
}
