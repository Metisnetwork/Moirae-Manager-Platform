package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TokenInventoryManager;
import com.datum.platform.mapper.TokenInventoryMapper;
import com.datum.platform.mapper.domain.TokenInventory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenInventoryManagerImpl extends ServiceImpl<TokenInventoryMapper, TokenInventory> implements TokenInventoryManager {
}
