package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.TokenHolder;

import java.util.List;

public interface TokenHolderManager extends IService<TokenHolder> {

    boolean batchInsertOrUpdateByUser(String address, List<TokenHolder> tokenHolderList);

    TokenHolder getByUser(String userAddress, String metisPayAddress);
}
