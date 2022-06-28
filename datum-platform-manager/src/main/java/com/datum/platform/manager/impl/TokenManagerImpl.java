package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TokenManager;
import com.datum.platform.mapper.TokenMapper;
import com.datum.platform.mapper.domain.Token;
import com.datum.platform.mapper.enums.TokenTypeEnum;
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

    @Override
    public List<String> getERC20TokenAddressList() {
        LambdaQueryWrapper<Token> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Token::getAddress);
        wrapper.eq(Token::getType, TokenTypeEnum.ERC20);
        return listObjs(wrapper, Object::toString);
    }

    @Override
    public List<Token> listERC721Token() {
        LambdaQueryWrapper<Token> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Token::getType, TokenTypeEnum.ERC721);
        return list(wrapper);
    }
}
