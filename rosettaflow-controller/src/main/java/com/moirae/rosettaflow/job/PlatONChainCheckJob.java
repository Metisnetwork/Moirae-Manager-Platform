package com.moirae.rosettaflow.job;

import com.moirae.rosettaflow.chain.platon.PlatONClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PlatONChainCheckJob {

    @Resource
    private PlatONClient platONClient;

    /**
     * web3j实例保活
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void keepAlive() {
        log.info("PlatONChainCheckJob platOnClient begin");
        try {
            platONClient.selectBestNode();
            log.info("PlatONChainCheckJob platOnClient end");
        } catch (Exception e){
            log.error("PlatONChainCheckJob platOnClient error",e);
        }
    }

}
