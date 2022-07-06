package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.Token;

import java.util.List;

public interface TokenManager extends IService<Token> {

    List<Token> getNeedSyncedTokenList(int size);

    List<String> getERC20TokenAddressList();
}
