package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.chain.platon.contract.DataTokenTemplateContract;
import com.datum.platform.chain.platon.contract.ERC721TemplateContract;
import com.datum.platform.mapper.domain.Token;
import com.datum.platform.mapper.domain.TokenHolder;
import com.datum.platform.mapper.domain.TokenInventory;
import com.datum.platform.service.DataService;
import com.datum.platform.service.UserService;
import com.platon.tuples.generated.Tuple3;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncTokenInventoryInfoTask {

    @Resource
    private DataService dataService;
    @Resource
    private ERC721TemplateContract erc721TemplateContract;

    @Scheduled(fixedDelay = 60 * 1000)
    @Lock(keys = "SyncTokenInventoryInfoTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<Token> tokenList = dataService.listERC721Token();
            for (Token token: tokenList) {
                try{
                    sync(token);
                }catch (Exception e) {
                    log.error("Token Holder信息同步, 明细失败：{}", token.getAddress(), e);
                }
            }
        } catch (Exception e) {
            log.error("Token Holder信息同步失败原因：{}", e.getMessage(), e);
        }
        log.info("Token Holder信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void sync(Token token){
        List<TokenInventory> tokenInventoryList = new ArrayList<>();
        for (long i = 0; i < erc721TemplateContract.totalSupply(token.getAddress()).longValue(); i++) {
            BigInteger tokenId = erc721TemplateContract.tokenByIndex(token.getAddress(), BigInteger.valueOf(i));
            Tuple3<String, String, Boolean> tuple3 = erc721TemplateContract.getExtInfo(token.getAddress(), tokenId);
            TokenInventory tokenInventory = new TokenInventory();
            tokenInventory.setTokenAddress(token.getAddress());
            tokenInventory.setTokenId(tokenId.toString());
            tokenInventory.setMetaDataId(token.getMetaDataId());
            tokenInventory.setOwner(tuple3.getValue1());
            tokenInventory.setCharacteristic(tuple3.getValue2());
            if(tuple3.getValue3() == true) {
                tokenInventory.setIsSupportCtAlg(true);
                tokenInventory.setIsSupportPtAlg(false);
            }else {
                tokenInventory.setIsSupportCtAlg(false);
                tokenInventory.setIsSupportPtAlg(true);
            }
            tokenInventoryList.add(tokenInventory);
        }
        dataService.saveOrUpdateBatchTokenInventory(token.getAddress(), tokenInventoryList);
    }
}
