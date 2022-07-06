package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.WorkflowRunStatusCertificate;

import java.util.List;

/**
 * <p>
 * 工作流任务运行状态 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowRunStatusCertificateManager extends IService<WorkflowRunStatusCertificate> {

    List<WorkflowRunStatusCertificate> listByWorkflowRunId(Long workflowRunId);
}
