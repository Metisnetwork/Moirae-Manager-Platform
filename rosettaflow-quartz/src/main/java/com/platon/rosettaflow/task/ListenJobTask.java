package com.platon.rosettaflow.task;

import com.alibaba.fastjson.JSON;
import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.utils.RedisUtil;
import com.platon.rosettaflow.mapper.domain.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 监听redis中的新增及修改job任务，并根据任务创建quartz来管理作业
 */
@Slf4j
@Component
public class ListenJobTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private JobManager jobManager;

    @Resource
    private RedisUtil redisUtil;

    @Scheduled(fixedDelay = 3000, initialDelay = 1000)
    public void run() {
        if (!sysConfig.isMasterNode()) {
            return;
        }
        //获取新增job列表
        long size = redisUtil.listSize(SysConstant.JOB_ADD_QUEUE);
        for (int i = 0; i < size; i++) {
            String jobJson = redisUtil.listRightPop(SysConstant.JOB_ADD_QUEUE);
            Job job = JSON.parseObject(jobJson, Job.class);
            jobManager.startJob(job);
        }

        //获取修改job列表
        size = redisUtil.listSize(SysConstant.JOB_EDIT_QUEUE);
        for (int i = 0; i < size; i++) {
            String jobJson = redisUtil.listRightPop(SysConstant.JOB_ADD_QUEUE);
            Job job = JSON.parseObject(jobJson, Job.class);
            jobManager.startJob(job);
        }
        //获取暂停ob列表
        size = redisUtil.listSize(SysConstant.JOB_PAUSE_QUEUE);
        for (int i = 0; i < size; i++) {
            String jobJson = redisUtil.listRightPop(SysConstant.JOB_PAUSE_QUEUE);
            Job job = JSON.parseObject(jobJson, Job.class);
            jobManager.pauseJob(job);
        }
    }
}
