package com.datum.platform.init;

import com.datum.platform.chain.platon.contract.IUniswapV2FactoryContract;
import com.datum.platform.chain.platon.contract.VoteContract;
import com.datum.platform.service.OrgService;
import com.datum.platform.service.PublicityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.TimeZone;

/**
 * 进程初始化
 */
@Slf4j
@Component
public class ProcessInit {

    @Resource
    private OrgService orgService;
    @Resource
    private PublicityService publicityService;
    @Resource
    private IUniswapV2FactoryContract uniswapV2FactoryContract;
    @Resource
    private VoteContract voteContract;

    @PostConstruct
    public void init() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // 第一次启动时初始化公共组织
        orgService.initPublicOrg();
        // 初始化工厂合约地址
        uniswapV2FactoryContract.initContractAddress();
        // 初始化投票合约
        voteContract.init();
        // 添加提案消费
        publicityService.subscribe();
    }
}
