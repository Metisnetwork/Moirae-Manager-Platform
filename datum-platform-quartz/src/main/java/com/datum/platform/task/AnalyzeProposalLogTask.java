//package com.datum.platform.task;
//
//import cn.hutool.core.date.DateUtil;
//import com.datum.platform.mapper.domain.StatsMetaData;
//import com.datum.platform.mapper.domain.Task;
//import com.datum.platform.mapper.enums.DataSyncTypeEnum;
//import com.datum.platform.service.StatisticsService;
//import com.datum.platform.service.SysService;
//import com.datum.platform.service.TaskService;
//import com.zengtengpeng.annotation.Lock;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 同步元数据定时任务, 多久同步一次待确认
// *
// * @author hudenian
// * @date 2021/8/23
// */
//@ConditionalOnProperty(name="dev.quartz", havingValue="true")
//@Slf4j
//@Component
//public class AnalyzeProposalLogTask {
//    @Resource
//    private SysService sysService;
//    @Resource
//    private TaskService taskService;
//    @Resource
//    private StatisticsService statisticsService;
//
//    @Scheduled(fixedDelay = 5 * 1000)
//    @Lock(keys = "AnalyzeProposalLogTask")
//    public void run() {
//        long begin = DateUtil.current();
//        try {
//            sysService.sync(DataSyncTypeEnum.ANALYZE_PROPOSAL_LOG.getDataType(),DataSyncTypeEnum.ANALYZE_PROPOSAL_LOG.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
//                    (latestSynced, size) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
//                        return taskService.listTaskDetail(latestSynced, size);
//                    },
//                    (taskDetailList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
//                        // 批量更新
//                        return this.analyzeTaskDetail(taskDetailList);
//                    });
//        } catch (Exception e) {
//            log.error("任务分析,失败原因：{}", e.getMessage(), e);
//        }
//        log.info("任务分析结束，总耗时:{}ms", DateUtil.current() - begin);
//    }
//
//    private long analyzeTaskDetail(List<Task> taskDetailList) {
//        Map<String, StatsMetaData> save = new HashMap<>();
//        taskDetailList.stream()
//                .flatMap(task -> task.getDataProviderList().stream())
//                .forEach(dataProvider -> {
//                    if(save.containsKey(dataProvider.getMetaDataId())){
//                        StatsMetaData statsMetaData = save.get(dataProvider.getMetaDataId());
//                        statsMetaData.setUsageCount(statsMetaData.getUsageCount() + 1);
//                        return ;
//                    }
//
//                    StatsMetaData statsMetaData = statisticsService.getStatsMetaDataById(dataProvider.getMetaDataId());
//                    if(statsMetaData != null ){
//                        statsMetaData.setUsageCount(statsMetaData.getUsageCount() + 1);
//                        save.put(dataProvider.getMetaDataId(), statsMetaData);
//                    }
//                });
//        statisticsService.saveOrUpdateBatchStatsMetaData(save.values());
//        return taskDetailList.get(taskDetailList.size() - 1).getSyncSeq();
//    }
//}
