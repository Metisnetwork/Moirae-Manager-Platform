package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.SubJobDto;
import com.platon.rosettaflow.mapper.domain.SubJob;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 子作业管理服务接口
 */
public interface ISubJobService extends IService<SubJob> {

    /**
     * 获取子作业分页列表
     *
     * @param current  当前页
     * @param size     每页大小
     * @param subJobId  子作业Id
     * @param jobId  作业Id
     * @return 分页数据
     */
    IPage<SubJobDto> sublist(Long current, Long size, String subJobId, Long jobId);

    /**
     *  暂停子作业
     * @param id 子作业Id
     */
    void pause(Long id);

    /**
     * 重启子作业
     * @param id 子作业Id
     */
    void reStart(Long id);

    /**
     *  修改子作业状态
     * @param subJobId 子作业id
     * @param runStatus 子作业状态
     */
    void updateJobStatus(Long subJobId, Byte runStatus);
}
