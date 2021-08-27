package com.platon.rosettaflow.task;

import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.mapper.domain.Job;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 监听redis中的新增及修改job任务，并根据任务创建quartz来管理作业
 */
public class ListenJobTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private JobManager jobManager;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Scheduled(fixedDelay = 3000, initialDelay = 10000)
    public void run() {
        if (!sysConfig.isMasterNode()) {
            return;
        }
        //获取新增job列表
        List<Job> addJobList = (List<Job>) redisTemplate.opsForList().rightPop(SysConstant.JOB_ADD_QUEUE);
        for (int i = 0; i < addJobList.size(); i++) {
            Job job = addJobList.get(i);
            jobManager.startJob(job, false);
        }

        //获取修改的job列表
        List<Job> editJobList = (List<Job>) redisTemplate.opsForList().rightPop(SysConstant.JOB_EDIT_QUEUE);
        for (int i = 0; i < editJobList.size(); i++) {
            Job job = editJobList.get(i);
            jobManager.startJob(job, true);
        }
    }
}
