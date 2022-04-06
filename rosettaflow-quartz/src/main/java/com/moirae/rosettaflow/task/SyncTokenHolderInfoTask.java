//package com.moirae.rosettaflow.task;
//
//import cn.hutool.core.date.DateUtil;
//import com.moirae.rosettaflow.chain.platon.contract.DataTokenTemplateDao;
//import com.moirae.rosettaflow.mapper.domain.Token;
//import com.moirae.rosettaflow.mapper.domain.TokenHolder;
//import com.moirae.rosettaflow.service.DataService;
//import com.moirae.rosettaflow.service.UserService;
//import com.zengtengpeng.annotation.Lock;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@Component
//public class SyncTokenHolderInfoTask {
//
//    @Resource
//    private DataService dataService;
//    @Resource
//    private UserService userService;
//    @Resource
//    private DataTokenTemplateDao dataTokenTemplateDao;
//
//    @Scheduled(fixedDelay = 60 * 1000)
//    @Lock(keys = "SyncTokenHolderInfoTask")
//    public void run() {
//        long begin = DateUtil.current();
//        try {
//            //查询所有用户地址
//            List<String> addressList = userService.getIdList();
//            //查询所有数据token地址
//            List<String> tokenList = dataService.getTokenIdList();
//            for (String token: tokenList) {
//                sync(token, addressList);
//            }
//        } catch (Exception e) {
//            log.error("Token Holder信息同步失败原因：{}", e.getMessage(), e);
//        }
//        log.info("Token Holder信息同步，总耗时:{}ms", DateUtil.current() - begin);
//    }
//
//    private void sync(String token, List<String> addressList){
//        List<TokenHolder> tokenHolderList = new ArrayList<>();
//        for (String address: addressList) {
//            TokenHolder tokenHolder = syncUser(token, address);
//            if(tokenHolder != null){
//
//            }
//        }
//    }
//
//    private TokenHolder syncUser(String token, String address){
//        BigInteger balance = dataTokenTemplateDao.balanceOf(token, address);
//        BigInteger allowance = dataTokenTemplateDao.allowance(token, address);
//        if(balance.compareTo(BigInteger.ZERO) >= 0 || allowance.compareTo(BigInteger.ZERO) >= 0){
//            TokenHolder tokenHolder = new TokenHolder();
//            tokenHolder.setTokenAddress(token);
//            tokenHolder.setAddress(address);
//            tokenHolder.setBalance(balance.toString());
//            tokenHolder.setAuthorizeBalance(allowance.toString());
//            return tokenHolder;
//        }
//        return null;
//    }
//}
