package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TokenManager;
import com.moirae.rosettaflow.mapper.TokenMapper;
import com.moirae.rosettaflow.mapper.domain.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TokenManagerImpl extends ServiceImpl<TokenMapper, Token> implements TokenManager {
    @Override
    public List<Token> getNeedSyncedTokenList(int size) {
        LambdaQueryWrapper<Token> wrapper = Wrappers.lambdaQuery();
        wrapper.isNull(Token::getName);
        wrapper.last(" limit " + size);
        return list(wrapper);
    }
}
