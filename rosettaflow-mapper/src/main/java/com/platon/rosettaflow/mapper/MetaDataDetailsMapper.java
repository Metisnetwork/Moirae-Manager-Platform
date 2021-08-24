package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.MetaDataDetails;

/**
 * @author admin
 */
public interface MetaDataDetailsMapper extends BaseMapper<MetaDataDetails> {
    void truncate();
}