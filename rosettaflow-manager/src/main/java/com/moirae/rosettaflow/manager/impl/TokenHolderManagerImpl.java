package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TokenHolderManager;
import com.moirae.rosettaflow.mapper.TokenHolderMapper;
import com.moirae.rosettaflow.mapper.domain.TokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TokenHolderManagerImpl extends ServiceImpl<TokenHolderMapper, TokenHolder> implements TokenHolderManager {

    @Override
    public boolean batchInsertOrUpdateByUser(String address, List<TokenHolder> tokenHolderList) {
        // 查询用户的账户信息
        Set<String> tokenAddressSet = listByUser(address).stream().collect(Collectors.toSet());

        List<TokenHolder> insertList = tokenHolderList.stream().filter(item -> !tokenAddressSet.contains(item.getTokenAddress())).collect(Collectors.toList());
        List<TokenHolder> updateList = tokenHolderList.stream().filter(item -> tokenAddressSet.contains(item.getTokenAddress())).collect(Collectors.toList());

        if(insertList.size()>0){
            saveBatch(insertList);
        }

        if(updateList.size()>0){
            baseMapper.updateBatch(updateList);
        }
        return true;
    }

    @Override
    public TokenHolder getById(String tokenAddress, String userAddress) {
        LambdaQueryWrapper<TokenHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TokenHolder::getTokenAddress, tokenAddress);
        wrapper.eq(TokenHolder::getAddress, userAddress);
        return getOne(wrapper);
    }

    private List<String> listByUser(String address){
        LambdaQueryWrapper<TokenHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TokenHolder::getAddress, address);
        return listObjs(wrapper, item -> item.toString());
    }
}
