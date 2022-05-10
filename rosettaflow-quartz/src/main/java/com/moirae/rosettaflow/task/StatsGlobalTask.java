package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.PowerServer;
import com.moirae.rosettaflow.mapper.domain.StatsGlobal;
import com.moirae.rosettaflow.mapper.domain.Task;
import com.moirae.rosettaflow.service.*;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
public class StatsGlobalTask {

    @Resource
    private StatisticsService statisticsService;
    @Resource
    private TaskService taskService;
    @Resource
    private UserService userService;
    @Resource
    private DataService dataService;
    @Resource
    private PowerService powerService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "StatsGlobalTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            StatsGlobal global = statisticsService.globalStats();
            Task task = taskService.statisticsOfGlobal();
            global.setTaskCount(task.getTaskCount());
            global.setAddressCountOfTask(task.getAddressCount());
            global.setDataTokenUsed(task.getDataCount());
            MetaData data = dataService.statisticsOfGlobal();
            global.setDataSize(data.getTotalSize());
            global.setDataTokenCount(data.getTotalCount());
            global.setAddressCountOfActive(userService.countOfActiveAddress());
            PowerServer powerServer = powerService.statisticsOfGlobal();
            global.setTotalCore(powerServer.getOrgTotalCore());
            global.setTotalBandwidth(powerServer.getOrgTotalBandwidth());
            global.setTotalMemory(powerServer.getOrgTotalMemory());
            statisticsService.updateStatsGlobal(global);
        } catch (Exception e) {
            log.error("StatsGlobalTask, 失败原因：{}", e.getMessage(), e);
        }
        log.info("StatsGlobalTask，总耗时:{}ms", DateUtil.current() - begin);
    }
}
