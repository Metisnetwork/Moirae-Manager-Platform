package com.moirae.rosettaflow.init;

import com.moirae.rosettaflow.chain.platon.contract.IUniswapV2FactoryContract;
import com.moirae.rosettaflow.service.OrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 进程初始化
 */
@Slf4j
@Component
public class ProcessInit {

    @Resource
    private OrgService organizationService;
    @Resource
    private IUniswapV2FactoryContract uniswapV2FactoryDao;

    @PostConstruct
    public void init() throws Exception {
        // 第一次启动时初始化公共组织
        organizationService.initPublicOrg();
        // 初始化工厂合约地址
        uniswapV2FactoryDao.initContractAddress();
    }
}
