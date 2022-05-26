package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.common.utils.AddressChangeUtils;
import com.datum.platform.grpc.client.impl.GrpcSysServiceClientImpl;
import com.datum.platform.grpc.service.YarnNodeInfo;
import com.datum.platform.mapper.domain.OrgExpand;
import com.datum.platform.service.OrgService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据中心的组织同步
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncDcOrgWalletTask {

    @Resource
    private GrpcSysServiceClientImpl sysServiceClient;
    @Resource
    private OrgService organizationService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncDcOrgWalletTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<OrgExpand> orgExpandList = organizationService.getOrgExpandList();
            List<OrgExpand> updateList = new ArrayList<>();
            for (OrgExpand org: orgExpandList) {
                try {
                    OrgExpand update = syncOrgWallet(org);
                    if(update != null){
                        updateList.add(update);
                    }
                } catch (Exception e){
                    log.error("组织钱包信息同步, 明细失败：{}",org.getIdentityId(), e);
                }
            }
            if(updateList.size()>0){
                organizationService.batchUpdateOrgExpand(updateList);
            }
        } catch (Exception e) {
            log.error("组织钱包信息同步,从net同步元数据失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("组织钱包信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private OrgExpand syncOrgWallet(OrgExpand org) {
        YarnNodeInfo yarnNodeInfo = sysServiceClient.getNodeInfo(organizationService.getChannel(org.getIdentityId()));
        if(yarnNodeInfo != null){
            String rawAddress =  yarnNodeInfo.getObserverProxyWalletAddress();
            if(StringUtils.isBlank(rawAddress) || StringUtils.equalsAnyIgnoreCase(rawAddress, "0x0000000000000000000000000000000000000000")){
                return null;
            }
            rawAddress = rawAddress.toLowerCase();
            String address = AddressChangeUtils.convert0xAddress(rawAddress);
            if(!address.equals(org.getObserverProxyWalletAddress())){
                org.setObserverProxyWalletAddress(address);
                return org;
            }
        }
        return null;
    }
}
