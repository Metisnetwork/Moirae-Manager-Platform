package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.mapper.domain.MetaData;
import com.datum.platform.mapper.domain.StatsToken;
import com.datum.platform.service.DataService;
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
public class StatsTokenTask {

    @Resource
    private DataService dataService;
    @Resource
    private TaskService taskService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "StatsTokenTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<String> tokenAddressList = dataService.listERC20TokenAddress();
            List<StatsToken> saveList = new ArrayList<>();
            for (String tokenAddress: tokenAddressList) {
                try {
                    StatsToken save = new StatsToken();
                    save.setAddress(tokenAddress);
                    List<MetaData> metaDataList = dataService.listMetaDataByTokenAddress(tokenAddress);
                    if(metaDataList.size() > 0){
                        save.setTokenUsed(taskService.countOfTokenUsed(metaDataList.stream().map(MetaData::getMetaDataId).collect(Collectors.toList())));
                    }else {
                        save.setTokenUsed(0L);
                    }
                    saveList.add(save);
                } catch (Exception e){
                    log.error("StatsTokenTask, 明细失败：{}",tokenAddress, e);
                }
            }

            if(saveList.size() > 0){
                dataService.saveOrUpdateBatchStatsToken(saveList);
            }
        } catch (Exception e) {
            log.error("StatsTokenTask, 失败原因：{}", e.getMessage(), e);
        }
        log.info("StatsTokenTask，总耗时:{}ms", DateUtil.current() - begin);
    }

}
