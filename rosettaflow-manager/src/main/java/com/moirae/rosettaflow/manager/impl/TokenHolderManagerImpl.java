package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TokenHolderManager;
import com.moirae.rosettaflow.manager.TokenManager;
import com.moirae.rosettaflow.mapper.TokenHolderMapper;
import com.moirae.rosettaflow.mapper.TokenMapper;
import com.moirae.rosettaflow.mapper.domain.Token;
import com.moirae.rosettaflow.mapper.domain.TokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenHolderManagerImpl extends ServiceImpl<TokenHolderMapper, TokenHolder> implements TokenHolderManager {
}
