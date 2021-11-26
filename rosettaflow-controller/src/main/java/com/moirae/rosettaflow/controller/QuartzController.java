package com.moirae.rosettaflow.controller;

import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.quartz.job.TestJob;
import com.moirae.rosettaflow.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @author admin
 * @date 2021/8/2
 */
@Slf4j
@RestController
@Api(tags = "定时任务处理相关接口")
@RequestMapping(value = "quartz", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuartzController {

    static final String GROUP = "MOIRAE";

    @Resource
    private Scheduler scheduler;

    @PostMapping("create")
    @ApiOperation(value = "创建一个定时任务", notes = "创建一个定时任务")
    public ResponseVo<?> register(@RequestParam("orderNo") String orderNo) {
        log.info("创建一个定时任务");

        Date start = new Date(System.currentTimeMillis() + 3 * 1000);
        Date end = new Date(System.currentTimeMillis() + 300 * 1000);

        //通过JobBuilder.newJob()方法获取到当前Job的具体实现(以下均为链式调用)
        // 这里是固定Job创建，所以代码写死XXX.class
        // 如果是动态的，根据不同的类来创建Job，则 ((Job)Class.forName("com.zy.job.TestJob").newInstance()).getClass()
        // 即是 JobBuilder.newJob(((Job)Class.forName("com.zy.job.TestJob").newInstance()).getClass())
        JobDetail jobDetail = JobBuilder.newJob(TestJob.class)
                //给当前JobDetail添加参数，K V形式
                .usingJobData("name", "zy")
                //给当前JobDetail添加参数，K V形式，链式调用，可以传入多个参数，在Job实现类中，可以通过jobExecutionContext.getJobDetail().getJobDataMap().get("age")获取值
                .usingJobData("age", 23)
                //添加认证信息，有3种重写的方法，我这里是其中一种，可以查看源码看其余2种
                .withIdentity(orderNo, GROUP)
                .build();//执行

        Trigger trigger = TriggerBuilder.newTrigger()
                //给当前JobDetail添加参数，K V形式，链式调用，可以传入多个参数，在Job实现类中，可以通过jobExecutionContext.getTrigger().getJobDataMap().get("orderNo")获取值
                .usingJobData("orderNo", orderNo)
                //添加认证信息，有3种重写的方法，我这里是其中一种，可以查看源码看其余2种
                .withIdentity(orderNo, GROUP)
                //立即生效
                //.startNow()
                //开始执行时间
                .startAt(start)
                //结束执行时间
                .endAt(end)
                //添加执行规则，SimpleTrigger、CronTrigger的区别主要就在这里
                .withSchedule(
//                        SimpleScheduleBuilder.simpleSchedule()
//                                /**每隔1s执行一次*/
//                                .withIntervalInSeconds(3)
//                                /**一直执行，*/
//                                .repeatForever()
                        CronScheduleBuilder.cronSchedule("*/3 * * * * ?")
                )
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            System.err.println("--------定时任务启动成功 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ------------");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return ResponseVo.createSuccess("创建成功");
    }

    @PostMapping("deleteJob")
    @ApiOperation(value = "删除一个定时任务", notes = "删除一个定时任务")
    public ResponseVo<?> deleteJob(@RequestParam("orderNo") String orderNo) {
        try {
            //暂停触发器
            scheduler.pauseTrigger(TriggerKey.triggerKey(orderNo, GROUP));
            //移除触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(orderNo, GROUP));
            //删除Job
            scheduler.deleteJob(JobKey.jobKey(orderNo, GROUP));

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return ResponseVo.createSuccess("删除成功");
    }

    @GetMapping("getAllJob")
    @ApiOperation(value = "获取所有的jobKey", notes = "获取所有的jobKey")
    public ResponseVo<Set<JobKey>> getAllJob() {
        try {
            Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.groupEquals(GROUP));
            return ResponseVo.createSuccess(jobKeySet);
        } catch (SchedulerException e) {
            log.error("");
            e.printStackTrace();
        }
        return ResponseVo.create(RespCodeEnum.FAIL);
    }

    @GetMapping("getAllTrigger")
    @ApiOperation(value = "获取所有的触发器key", notes = "获取所有的触发器key")
    public ResponseVo<Set<TriggerKey>> getAllTrigger() {
        try {
            Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(GroupMatcher.groupEquals(GROUP));
            return ResponseVo.createSuccess(triggerKeySet);
        } catch (SchedulerException e) {
            log.error("");
            e.printStackTrace();
        }
        return ResponseVo.create(RespCodeEnum.FAIL);
    }
}
