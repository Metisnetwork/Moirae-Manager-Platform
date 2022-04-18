package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.grpc.client.impl.GrpcSysServiceClientImpl;
import com.moirae.rosettaflow.grpc.service.YarnNodeInfo;
import com.moirae.rosettaflow.mapper.domain.OrgExpand;
import com.moirae.rosettaflow.service.OrgService;
import com.platon.bech32.Bech32;
import com.platon.crypto.Credentials;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Scheduled(fixedDelay = 60 * 1000)
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
            organizationService.batchUpdateOrgExpand(updateList);
        } catch (Exception e) {
            log.error("组织钱包信息同步,从net同步元数据失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("组织钱包信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private OrgExpand syncOrgWallet(OrgExpand org) {
        YarnNodeInfo yarnNodeInfo = sysServiceClient.getNodeInfo(organizationService.getChannel(org.getIdentityId()));
        if(yarnNodeInfo != null){
            String address = mockAddress(org);
            if(StringUtils.isNotBlank(address) && !address.equals(org.getObserverProxyWalletAddress())){
                org.setObserverProxyWalletAddress(address);
                return org;
            }
        }
        return null;
    }

    private String mockAddress(OrgExpand org){
        Map<String, String> map = new HashMap<>();
        map.put("identity:3ddb63047d214ddd8187438a82841250", Bech32.addressDecodeHex(Credentials.create("68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f88").getAddress()));
        map.put("identity:e9eef460ea9c473993c6477915106eed", Bech32.addressDecodeHex(Credentials.create("68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f87").getAddress()));
        map.put("identity:f614f8ac21b44fe89926ad4f26ef5b07", Bech32.addressDecodeHex(Credentials.create("68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f86").getAddress()));
        return map.get(org.getIdentityId());
    }
}
