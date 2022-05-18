package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Workflow;

import java.util.Date;

/**
 * <p>
 * 工作流表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowManager extends IService<Workflow> {

    int getWorkflowCount(String address);

    IPage<Workflow> getWorkflowList(Page<Workflow> page, String address, String keyword, Long algorithmId, Date begin, Date end, Integer createMode);

    boolean updateStep(Long workflowId, Integer step, Boolean isSettingCompleted);

    Workflow increaseVersion(Long workflowId);

    Workflow delete(Long workflowId);

    Workflow createOfWizardMode(String workflowName, String workflowDesc, Long algorithmId, String algorithmName, Long calculationProcessId, String calculationProcessName, String address);

    Workflow createOfExpertMode(String workflowName, String address);

    void updateLastRunTime(Long workflowId);
}
