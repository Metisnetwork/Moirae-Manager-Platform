package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.MetaDataDetailsMapper;
import com.platon.rosettaflow.mapper.domain.MetaDataDetails;
import com.platon.rosettaflow.service.IMetaDataDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 元数据服务实现类
 */
@Slf4j
@Service
public class MetaDataDetailsServiceImpl extends ServiceImpl<MetaDataDetailsMapper, MetaDataDetails> implements IMetaDataDetailsService {
    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }
}
