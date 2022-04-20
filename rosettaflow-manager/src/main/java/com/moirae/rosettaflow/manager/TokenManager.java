package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Token;

import java.util.List;

public interface TokenManager extends IService<Token> {

    List<Token> getNeedSyncedTokenList(int size);

    List<String> getTokenIdList();
}
