package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.enums.JobStatusEnum;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.domain.Job;
import com.zengtengpeng.annotation.MQListener;
import com.zengtengpeng.annotation.MQPublish;
import com.zengtengpeng.operation.RedissonCollection;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hudenian
 * @date 2021/10/20
 */
@Slf4j
@RestController
@Api(tags = "redis_mq队列相关接口")
@RequestMapping(value = "redis", produces = MediaType.APPLICATION_JSON_VALUE)
public class MqController {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedissonCollection redissonCollection;

    @PostMapping("publishMsg")
    @ApiOperation(value = "往mq队列存放一个消息", notes = "往mq队列存放一个消息")
    @MQPublish(name = SysConstant.JOB_ADD_QUEUE)
    public Job publishMsg() {

        log.info("往mq队列存放一个消息");
        Job job = new Job();
        job.setId(1L);
        job.setWorkflowId(1L);
        job.setName("name");
        job.setDesc("desc");
        job.setRepeatFlag((byte) 1);
        job.setRepeatInterval(1);
        job.setBeginTime(new Date());
        job.setEndTime(new Date());
        job.setJobStatus(JobStatusEnum.RUNNING.getValue());
        job.setStatus(StatusEnum.VALID.getValue());
        job.setCreateTime(new Date());
        job.setUpdateTime(new Date());
        return job;
    }

    @MQListener(name = "test")
    public void test1(CharSequence charSequence, String o, Object object) {
        System.out.println("收到消息mq队列消息" + o);
        System.out.println("收到消息mq队列消息" + object);
    }

}
