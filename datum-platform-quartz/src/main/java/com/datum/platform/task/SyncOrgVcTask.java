package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.chain.platon.contract.VoteContract;
import com.datum.platform.mapper.domain.OrgExpand;
import com.datum.platform.mapper.domain.OrgVc;
import com.datum.platform.mapper.domain.Publicity;
import com.datum.platform.service.OrgService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据中心的组织同步
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncOrgVcTask {

    @Resource
    private OrgService orgService;

//    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncOrgVcTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<OrgExpand> orgExpandList = orgService.listHaveIpPortOrgExpand();
            Set<String> vcSet = orgService.listOrgVcId().stream().collect(Collectors.toSet());
            List<OrgVc> orgVcList = new ArrayList<>();
            List<Publicity> publicityList = new ArrayList<>();
            orgExpandList.stream()
                    .filter(orgExpand -> !vcSet.contains(orgExpand.getIdentityId()))
                    .forEach(orgExpand -> {
                        //TODO

                    }
            );
            if(orgVcList.size() > 0){
                orgService.batchSaveOrgVc(orgVcList, publicityList);
            }
        } catch (Exception e) {
            log.error("组织VC信息,失败原因：{}", e.getMessage(), e);
        }
        log.info("组织VC信息，总耗时:{}ms", DateUtil.current() - begin);
    }
}
