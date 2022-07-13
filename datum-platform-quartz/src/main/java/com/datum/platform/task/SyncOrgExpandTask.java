package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.chain.platon.contract.VoteContract;
import com.datum.platform.common.utils.AddressChangeUtils;
import com.datum.platform.mapper.domain.OrgExpand;
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
public class SyncOrgExpandTask {

    @Resource
    private OrgService orgService;
    @Resource
    private VoteContract voteContract;

//    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncOrgExpandTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<OrgExpand> orgExpandList = orgService.listOrgExpand();
            List<OrgExpand> updateOrgExpandList = new ArrayList<>();
            Set<String> authoritySet = voteContract.getAllAuthority().getValue1().stream().collect(Collectors.toSet());
            Set<String> vcSet = orgService.listOrgVcId().stream().collect(Collectors.toSet());
            orgExpandList.forEach(orgExpand -> {
                if(authoritySet.contains(AddressChangeUtils.did20xAddress(orgExpand.getIdentityId())) && !orgExpand.getIsAuthority()){
                    orgExpand.setIsAuthority(true);
                    updateOrgExpandList.add(orgExpand);
                }
                if(!authoritySet.contains(AddressChangeUtils.did20xAddress(orgExpand.getIdentityId())) && orgExpand.getIsAuthority()){
                    orgExpand.setIsAuthority(false);
                    updateOrgExpandList.add(orgExpand);
                }
                if(vcSet.contains(AddressChangeUtils.did20xAddress(orgExpand.getIdentityId())) && !orgExpand.getIsCertified()){
                    orgExpand.setIsCertified(true);
                    updateOrgExpandList.add(orgExpand);
                }
                if(!vcSet.contains(AddressChangeUtils.did20xAddress(orgExpand.getIdentityId())) && orgExpand.getIsCertified()){
                    orgExpand.setIsCertified(false);
                    updateOrgExpandList.add(orgExpand);
                }
            });
            if(updateOrgExpandList.size() > 0){
                orgService.batchUpdateOrgExpand(updateOrgExpandList);
            }
        } catch (Exception e) {
            log.error("组织信息扩展信息,失败原因：{}", e.getMessage(), e);
        }
        log.info("组织信息扩展信息，总耗时:{}ms", DateUtil.current() - begin);
    }
}
