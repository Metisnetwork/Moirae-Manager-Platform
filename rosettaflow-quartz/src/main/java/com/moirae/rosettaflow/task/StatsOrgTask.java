package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.mapper.domain.StatsGlobal;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;
import com.moirae.rosettaflow.service.OrgService;
import com.moirae.rosettaflow.service.StatisticsService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
public class StatsOrgTask {

    @Resource
    private OrgService orgService;
    @Resource
    private StatisticsService statisticsService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "StatsOrgTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<String> orgIdList = orgService.getEffectiveOrgIdList();
            List<StatsOrg> saveList = new ArrayList<>();
            StatsGlobal global = statisticsService.globalStats();
            for (String orgId: orgIdList) {
                try {
                    StatsOrg save = stats(orgId);
                    if(ObjectUtils.isNotEmpty(save)){
                        // 计算算力
                        save.setComputingPowerRatio(computingPowerRatio(global.getTotalCore(), global.getTotalMemory(), global.getTotalBandwidth(),
                                save.getOrgTotalCore(), save.getOrgTotalMemory(), save.getOrgTotalBandwidth()));

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

    private Integer computingPowerRatio(int totalCore, long totalMemory, long totalBandwidth, int orgTotalCore, long orgTotalMemory, long orgTotalBandwidth) {
        BigDecimal totalCoreBD = BigDecimal.valueOf(totalCore);
        BigDecimal totalMemoryBD = BigDecimal.valueOf(totalMemory);
        BigDecimal totalBandwidthBD = BigDecimal.valueOf(totalBandwidth);
        BigDecimal orgTotalCoreBD = BigDecimal.valueOf(orgTotalCore);
        BigDecimal orgTotalMemoryBD = BigDecimal.valueOf(orgTotalMemory);
        BigDecimal orgTotalBandwidthBD = BigDecimal.valueOf(orgTotalBandwidth);
        BigDecimal molecularOfCore = orgTotalCoreBD.multiply(totalMemoryBD).multiply(totalBandwidthBD);
        BigDecimal molecularOfMemory = orgTotalMemoryBD.multiply(totalCoreBD).multiply(totalBandwidthBD);
        BigDecimal molecularOfBandwidth = orgTotalBandwidthBD.multiply(totalMemoryBD).multiply(totalCoreBD);
        BigDecimal molecular = molecularOfCore.add(molecularOfMemory).add(molecularOfBandwidth).divide(BigDecimal.valueOf(3));
        BigDecimal denominator = totalCoreBD.multiply(totalMemoryBD).multiply(totalBandwidthBD);
        if(denominator.compareTo(BigDecimal.ZERO) > 0){
          return molecular.divide(denominator, 4, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(10000L)).intValue();
        }
        return 0;
    }

    private StatsOrg stats(String identityId){
        return orgService.getStatsOrg(identityId);
    }
}
