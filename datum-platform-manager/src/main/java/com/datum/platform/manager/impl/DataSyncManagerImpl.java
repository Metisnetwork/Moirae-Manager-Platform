package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.DataSyncManager;
import com.datum.platform.mapper.DataSyncMapper;
import com.datum.platform.mapper.domain.DataSync;
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
