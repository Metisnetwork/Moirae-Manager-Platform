package com.platon.rosettaflow.mq;

import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.mapper.domain.Job;
import com.platon.rosettaflow.task.JobManager;
import com.zengtengpeng.annotation.MQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hudenian
 * @date 2021/10/20
 * @description 监听作业事件
 */
@Slf4j
@Component
public class JobListener {

    @Resource
    private JobManager jobManager;

    @MQListener(name = SysConstant.JOB_ADD_QUEUE)
    @SuppressWarnings("unused")
    public void listenJobAdd(Object object) {
        Job job = (Job) object;
        log.info("listenJobAdd receive one message:{}", job.toString());
        jobManager.startJob(job);
    }

    @MQListener(name = SysConstant.JOB_EDIT_QUEUE)
    @SuppressWarnings("unused")
    public void listenJobEdit(Object object) {
        Job job = (Job) object;
        log.info("listenJobEdit receive one message:{}", job.toString());
        jobManager.startJob(job);
    }

    @MQListener(name = SysConstant.JOB_PAUSE_QUEUE)
    @SuppressWarnings("unused")
    public void listenJobPause(Object object) {
        Job job = (Job) object;
        log.info("listenJobPause receive one message:{}", job.toString());
        jobManager.pauseJob(job);
    }


}
