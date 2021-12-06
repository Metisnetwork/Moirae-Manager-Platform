package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.SubJobDto;
import com.moirae.rosettaflow.mapper.domain.SubJob;

import java.util.List;

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
     * 批量查询子作业信息
     * @param jobIds 作业id集合
     * @return List<SubJob> 子作业集合
     */
    List<SubJob> queryBatchSubJobListByJobId(List<Long> jobIds);

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

    /**
     * 批量修改子作业运行状态
     *
     * @param subJobList 子作业集合
     */
    void updateBatchRunStatus(List<SubJob> subJobList);

    /**
     * 批量修改子作业有效状态
     *
     * @param ids 子作业ids
     * @param status  有效状态
     */
    void updateBatchStatus(Object[] ids, Byte status);

    /**
     * 删除子作业
     * @param id 子作业id
     */
    void deleteSubJobById(Long id);

    /**
     *  批量删除子作业
     * @param ids 子作业ids集合
     */
    void deleteBatchSubJob(List<Long> ids);
}
