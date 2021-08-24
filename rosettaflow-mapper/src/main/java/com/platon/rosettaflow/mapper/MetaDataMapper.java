package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.MetaData;

/**
 * @author admin
 */
public interface MetaDataMapper extends BaseMapper<MetaData> {
    void truncate();
}