package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.MetaDataAuthManager;
import com.moirae.rosettaflow.mapper.MetaDataAuthMapper;
import com.moirae.rosettaflow.mapper.domain.MetaDataAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetaDataAuthManagerImpl extends ServiceImpl<MetaDataAuthMapper, MetaDataAuth> implements MetaDataAuthManager {

}
