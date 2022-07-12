package com.datum.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.manager.PublicityManager;
import com.datum.platform.mapper.domain.Publicity;
import com.datum.platform.service.SysService;
import com.datum.platform.service.dto.sys.PlatONPropertiesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SysServiceImpl implements SysService {

    @Resource
    private PlatONProperties platONProperties;
    @Resource
    private PublicityManager publicityManager;

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

    @Override
    public List<Publicity> listNeedSyncPublicity() {
        return publicityManager.listNeedSync();
    }

    @Override
    public void batchUpdatePublicity(List<Publicity> updateList) {
        publicityManager.updateBatchById(updateList);
    }
}
