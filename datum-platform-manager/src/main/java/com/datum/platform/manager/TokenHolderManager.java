package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.TokenHolder;

import java.util.List;

public interface TokenHolderManager extends IService<TokenHolder> {

    boolean batchInsertOrUpdateByUser(String address, List<TokenHolder> tokenHolderList);

    TokenHolder getById(String tokenAddress, String userAddress);
}
