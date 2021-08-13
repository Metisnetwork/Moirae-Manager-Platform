package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.JobDto;
import com.platon.rosettaflow.mapper.domain.Job;

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
}
