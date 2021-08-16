package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.JobStatusEnum;
import com.platon.rosettaflow.mapper.JobMapper;
import com.platon.rosettaflow.mapper.domain.Job;
import com.platon.rosettaflow.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 作业处理类
 */
@Slf4j
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    @Override
    public List<Job> getAllUnfinishedJob() {
        LambdaQueryWrapper<Job> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Job::getStatus, JobStatusEnum.UNFINISH.getValue());
        wrapper.le(Job::getBeginTime, new Date(System.currentTimeMillis()));
        wrapper.ge(Job::getEndTime, new Date(System.currentTimeMillis()));
        return this.baseMapper.selectList(wrapper);
    }
}
