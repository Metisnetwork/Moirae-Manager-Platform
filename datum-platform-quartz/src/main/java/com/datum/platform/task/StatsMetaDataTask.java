package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.mapper.domain.MetaData;
import com.datum.platform.mapper.domain.MetaDataCertificate;
import com.datum.platform.mapper.domain.StatsMetaDataCertificate;
import com.datum.platform.mapper.enums.DataSyncTypeEnum;
import com.datum.platform.service.DataService;
import com.datum.platform.service.DataSyncService;
import com.datum.platform.service.TaskService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
public class StatsMetaDataTask {

    @Resource
    private DataService dataService;
    @Resource
    private TaskService taskService;

    @Scheduled(fixedDelay = 60 * 1000)
    @Lock(keys = "StatsMetaDataTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<MetaDataCertificate> certificateList = dataService.listMetaDataCertificate();
            List<StatsMetaDataCertificate> saveList = new ArrayList<>();
            for (MetaDataCertificate certificate: certificateList) {
                try {
                    StatsMetaDataCertificate save = new StatsMetaDataCertificate();
                    save.setMetaDataCertificateId(certificate.getId());
                    save.setUsageCount(taskService.countOfMetaDataCertificateUsed(certificate.getMetaDataId(), certificate.getType(), certificate.getTokenAddress(), certificate.getTokenId()));
                    saveList.add(save);
                } catch (Exception e){
                    log.error("StatsMetaDataTask, 明细失败：{}",certificate, e);
                }
            }

            if(saveList.size() > 0){
                dataService.saveOrUpdateBatchStatsToken(saveList);
            }
        } catch (Exception e) {
            log.error("StatsMetaDataTask, 失败原因：{}", e.getMessage(), e);
        }
        log.info("StatsMetaDataTask，总耗时:{}ms", DateUtil.current() - begin);
    }
}
