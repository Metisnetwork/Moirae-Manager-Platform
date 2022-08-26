package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.chain.platon.enums.Web3jProtocolEnum;
import com.datum.platform.mapper.domain.OrgVc;
import com.datum.platform.service.OrgService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import network.platon.did.common.config.DidConfig;
import network.platon.did.sdk.base.dto.Credential;
import network.platon.did.sdk.contract.service.ContractService;
import network.platon.did.sdk.factory.PClient;
import network.platon.did.sdk.req.evidence.VerifyCredentialEvidenceReq;
import network.platon.did.sdk.resp.BaseResp;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据中心的组织同步
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncOrgVcTask {

    @Resource
    private OrgService orgService;
    @Resource
    private PlatONProperties platONProperties;

    @Scheduled(fixedDelay = 60 * 1000)
    @Lock(keys = "SyncOrgVcTask")
    public void run() {
        long begin = DateUtil.current();
        DidConfig.setCHAIN_ID(platONProperties.getChainId());
        DidConfig.setCHAIN_HRP(platONProperties.getHrp());
        DidConfig.setWeb3jProtocolEnum(platONProperties.getWeb3jProtocol() == Web3jProtocolEnum.HTTP ? network.platon.did.common.enums.Web3jProtocolEnum.HTTP : network.platon.did.common.enums.Web3jProtocolEnum.WS);
        DidConfig.setPLATON_URL(platONProperties.getWeb3jAddresses().get(0));
        DidConfig.setGAS_PRICE("10000000000");
        DidConfig.setGAS_LIMIT("4700000");
        DidConfig.setDID_CONTRACT_ADDRESS(platONProperties.getDidLatAddress());
        DidConfig.setPCT_CONTRACT_ADDRESS(platONProperties.getPctLatAddress());
        DidConfig.setVOTE_CONTRACT_ADDRESS(platONProperties.getVoteLatAddress());
        DidConfig.setCREDENTIAL_CONTRACT_ADDRESS(platONProperties.getCredentialLatAddress());
        ContractService.init();
        try {
            List<OrgVc> orgVcList = orgService.listNeedVerifyOrgVc();
            orgVcList.forEach(orgVc -> {
                try {
                    Credential credential = JSONObject.parseObject(orgVc.getVcContent(), Credential.class);
                    VerifyCredentialEvidenceReq credentialReq = VerifyCredentialEvidenceReq.builder().credential(credential).build();
                    BaseResp<String> result = PClient.createEvidenceClient().verifyCredentialEvidence(credentialReq);
                    if(result.checkSuccess()){
                        orgService.verifyOrgVcFinish(orgVc.getIdentityId(), 1);
                    }else{
                        log.error("vc 验证失败id = {} code ={} msg = {}",orgVc.getIdentityId(),  result.getCode(), result.getErrMsg());
                        orgService.verifyOrgVcFinish(orgVc.getIdentityId(), 2);
                    }
                } catch (Exception e) {
                    log.error("组织VC明细验证失败 id = " + orgVc.getIdentityId(), e);
                }
            });
        } catch (Exception e) {
            log.error("组织VC验证,失败原因：{}", e.getMessage(), e);
        }
        log.info("组织VC验证完成，总耗时:{}ms", DateUtil.current() - begin);
    }
}
