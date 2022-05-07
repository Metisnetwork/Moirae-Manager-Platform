package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.mapper.enums.StatsDayKeyEnum;
import com.moirae.rosettaflow.service.StatisticsService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
public class StatsDayTask {

    @Resource
    private StatisticsService statisticsService;

//    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "StatsDayTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            statisticsService.statisticsOfStatsDay(StatsDayKeyEnum.TASK_COUNT);
        } catch (Exception e) {
            log.error("StatsDayTask, 失败原因：{}", e.getMessage(), e);
        }
        log.info("StatsDayTask，总耗时:{}ms", DateUtil.current() - begin);
    }
}
