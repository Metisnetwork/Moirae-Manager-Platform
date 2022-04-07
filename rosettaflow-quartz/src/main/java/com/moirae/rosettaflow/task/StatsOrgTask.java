package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;
import com.moirae.rosettaflow.service.OrgService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class StatsOrgTask {

    @Resource
    private OrgService orgService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "StatsOrgTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<String> orgIdList = orgService.getEffectiveOrgIdList();
            List<StatsOrg> saveList = new ArrayList<>();
            for (String orgId: orgIdList) {
                try {
                    StatsOrg save = stats(orgId);
                    if(ObjectUtils.isNotEmpty(save)){
                        saveList.add(save);
                    }
                } catch (Exception e){
                    log.error("StatsOrgTask, 明细失败：{}",orgId, e);
                }
            }
            if(saveList.size() > 0){
                orgService.batchInsertOrUpdateStatsOrg(saveList);
            }
        } catch (Exception e) {
            log.error("StatsOrgTask, 失败原因：{}", e.getMessage(), e);
        }
        log.info("StatsOrgTask，总耗时:{}ms", DateUtil.current() - begin);
    }

    private StatsOrg stats(String identityId){
        return orgService.getStatsOrg(identityId);
    }
}
