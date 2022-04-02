package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.chain.platon.contract.DataTokenTemplateDao;
import com.moirae.rosettaflow.mapper.domain.Token;
import com.moirae.rosettaflow.service.DataService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Slf4j
@Component
public class SyncTokenInfoTask {

    @Resource
    private DataService dataService;
    @Resource
    private DataTokenTemplateDao dataTokenTemplateDao;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncTokenInfoTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<Token> tokenList = dataService.getNeedSyncedTokenList(1000);
            tokenList.forEach(item -> {
                try {
                    sync(item);
                } catch (Exception e){
                    log.error("DataToken信息同步, 明细失败：{}",item.getAddress(), e);
                }
            });
        } catch (Exception e) {
            log.error("DataToken信息同步失败原因：{}", e.getMessage(), e);
        }
        log.info("DataToken信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void sync(Token token){
        token.setSymbol(dataTokenTemplateDao.symbol(token.getAddress()));
        token.setName(dataTokenTemplateDao.name(token.getAddress()));
        BigInteger decimals = dataTokenTemplateDao.decimals(token.getAddress());
        if(decimals.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0){
            token.setDecimal(decimals.longValue());
        }
        dataService.updateToken(token);
    }
}
