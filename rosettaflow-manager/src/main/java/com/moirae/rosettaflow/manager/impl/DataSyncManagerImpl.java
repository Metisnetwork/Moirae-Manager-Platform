package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.DataSyncManager;
import com.moirae.rosettaflow.mapper.DataSyncMapper;
import com.moirae.rosettaflow.mapper.domain.DataSync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataSyncManagerImpl extends ServiceImpl<DataSyncMapper, DataSync> implements DataSyncManager {

    @Override
    public DataSync getOneByType(String type) {
        LambdaQueryWrapper<DataSync> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataSync::getDataType, type);
        return getOne(wrapper);
    }
}
