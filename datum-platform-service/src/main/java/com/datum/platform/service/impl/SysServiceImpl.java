package com.datum.platform.service.impl;

import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.service.SysService;
import com.datum.platform.service.dto.sys.PlatONPropertiesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class SysServiceImpl implements SysService {

    @Resource
    private PlatONProperties platONProperties;

    @Override
    public PlatONPropertiesDto getPlatONProperties() {
        PlatONPropertiesDto result = new PlatONPropertiesDto();
        result.setChainId(platONProperties.getChainId());
        result.setChainName(platONProperties.getChainName());
        result.setRpcUrl(platONProperties.getRpcUrl());
        result.setSymbol(platONProperties.getSymbol());
        result.setBlockExplorerUrl(platONProperties.getBlockExplorerUrl());
        result.setDatumNetworkPayAddress(platONProperties.getDatumNetworkPayAddress());
        result.setUniswapV2Router02(platONProperties.getUniswapV2Router02());
        result.setDexUrl(platONProperties.getDexUrl());
        return result;
    }
}
