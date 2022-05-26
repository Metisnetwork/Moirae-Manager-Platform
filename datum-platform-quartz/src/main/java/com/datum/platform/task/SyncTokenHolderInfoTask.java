package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.chain.platon.contract.DataTokenTemplateContract;
import com.datum.platform.mapper.domain.TokenHolder;
import com.datum.platform.service.DataService;
import com.datum.platform.service.UserService;
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
public class SyncTokenHolderInfoTask {

    @Resource
    private DataService dataService;
    @Resource
    private UserService userService;
    @Resource
    private DataTokenTemplateContract dataTokenTemplateDao;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncTokenHolderInfoTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            //查询所有在线用户地址
            List<String> addressList = userService.getOnlineUserIdList();
            //查询所有数据token地址
            List<String> tokenList = dataService.listTokenId();
            for (String address: addressList) {
                try{
                    sync(address, tokenList);
                }catch (Exception e) {
                    log.error("Token Holder信息同步, 明细失败：{}", address, e);
                }
            }
        } catch (Exception e) {
            log.error("Token Holder信息同步失败原因：{}", e.getMessage(), e);
        }
        log.info("Token Holder信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void sync(String address, List<String> tokenList){
        List<TokenHolder> tokenHolderList = new ArrayList<>();
        for (String token: tokenList) {
            TokenHolder tokenHolder = syncUser(address, token);
            if(tokenHolder != null){
                tokenHolderList.add(tokenHolder);
            }
        }
        if(tokenHolderList.size() > 0){
            dataService.saveOrUpdateBatchTokenHolder(address, tokenHolderList);
        }
    }

    private TokenHolder syncUser(String address, String token){
        BigInteger balance = dataTokenTemplateDao.balanceOf(token, address);
        BigInteger allowance = dataTokenTemplateDao.allowance(token, address);
        if(balance.compareTo(BigInteger.ZERO) > 0 || allowance.compareTo(BigInteger.ZERO) > 0){
            TokenHolder tokenHolder = new TokenHolder();
            tokenHolder.setTokenAddress(token);
            tokenHolder.setAddress(address);
            tokenHolder.setBalance(balance.toString());
            tokenHolder.setAuthorizeBalance(allowance.toString());
            return tokenHolder;
        }
        return null;
    }
}
