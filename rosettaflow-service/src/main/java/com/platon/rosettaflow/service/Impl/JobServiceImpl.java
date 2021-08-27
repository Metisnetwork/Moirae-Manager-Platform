package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.JobStatusEnum;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.JobDto;
import com.platon.rosettaflow.mapper.JobMapper;
import com.platon.rosettaflow.mapper.domain.Job;
import com.platon.rosettaflow.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Job> getAllUnfinishedJob() {
        LambdaQueryWrapper<Job> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Job::getStatus, JobStatusEnum.UNFINISH.getValue());
        wrapper.le(Job::getBeginTime, new Date(System.currentTimeMillis()));
        wrapper.ge(Job::getEndTime, new Date(System.currentTimeMillis()));
        wrapper.orderByAsc(Job::getId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(JobDto jobDto) {
        //保存作业
        Job job = new Job();
        BeanCopierUtils.copy(jobDto, job);
        job.setStatus(JobStatusEnum.UNFINISH.getValue());

        if(!this.save(job)){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ADD_ERROR.getMsg());
        }

        //将作业保存至redis待后续处理
        redisTemplate.opsForList().leftPush(SysConstant.JOB_ADD_QUEUE,job);
    }

    @Override
    public void edit(JobDto jobDto) {
        Job job = this.getById(jobDto.getId());
        if(null == job){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        BeanCopierUtils.copy(jobDto, job);
        job.setStatus(JobStatusEnum.UNFINISH.getValue());
        if(!this.updateById(job)){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_EDIT_ERROR.getMsg());
        }

        //将作业保存至redis待后续处理
        redisTemplate.opsForList().leftPush(SysConstant.JOB_EDIT_QUEUE,job);
    }
}
