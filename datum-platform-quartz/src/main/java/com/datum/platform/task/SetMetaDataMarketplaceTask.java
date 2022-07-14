package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.mapper.domain.MetaDataMarketplace;
import com.datum.platform.service.DataService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
public class SetMetaDataMarketplaceTask {

    @Resource
    private DataService dataService;

    @Scheduled(fixedDelay = 60 * 1000)
    @Lock(keys = "SetMetaDataMarketplaceTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<MetaDataMarketplace> metaDataMarketplaceList = dataService.listMetaDataIdOfPublished()
                    .stream()
                    .filter(metaDataId -> dataService.isTradable(metaDataId))
                    .map(metaDataId -> {
                        MetaDataMarketplace metaDataMarketplace = new MetaDataMarketplace();
                        metaDataMarketplace.setMetaDataId(metaDataId);
                        return metaDataMarketplace;
                    })
                    .collect(Collectors.toList());
            if(metaDataMarketplaceList.size() > 0){
                dataService.batchReplaceMetaDataMarketplace(metaDataMarketplaceList);
            }
        } catch (Exception e) {
            log.error("设置可公开的元数据, 失败原因：{}", e.getMessage(), e);
        }
        log.info("设置可公开的元数据，总耗时:{}ms", DateUtil.current() - begin);
    }
}
