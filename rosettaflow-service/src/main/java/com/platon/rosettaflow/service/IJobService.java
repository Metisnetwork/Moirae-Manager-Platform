package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.JobDto;
import com.platon.rosettaflow.mapper.domain.Job;
import com.platon.rosettaflow.mapper.domain.Workflow;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 作业管理服务
 */
public interface IJobService extends IService<Job> {
    /**
     * 获取所有待处理的作业
     *
     * @return 待处理作业列表
     */
    List<Job> getAllUnfinishedJob();

    /**
     * 获取作业分页列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @param jobName 作业名称
     * @return 分页数据
     */
    IPage<JobDto> list(Long current, Long size, String jobName);

    /**
     * 添加作业
     *
     * @param jobDto 添加作业请求对象
     */
    void add(JobDto jobDto);

    /**
     * 编辑作业
     *
     * @param jobDto 编辑作业请求对象
     */
    void edit(JobDto jobDto);

    /**
     * 查询关联工作流
     */
    List<Workflow> queryRelatedWorkflowName(Long projectId);

    /**
     * 暂停作业
     *
     * @param id 作业Id
     */
    void pause(Long id);

    /**
     * 重启作业
     *
     * @param id 作业Id
     */
    void reStart(Long id);

}
