package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.MetaDataManager;
import com.moirae.rosettaflow.mapper.MetaDataMapper;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetaDataManagerImpl extends ServiceImpl<MetaDataMapper, MetaData> implements MetaDataManager {

    @Override
    public IPage<MetaData> getDataListByOrg(Page<MetaData> page, String identityId) {
        return this.baseMapper.getDataListByOrg(page, identityId);
    }

    @Override
    public IPage<MetaData> getDataList(Page<MetaData> page, String keyword, String industry, Integer fileType, Long minSize, Long maxSize, String orderBy) {
        return this.baseMapper.getDataList(page, keyword, industry, fileType, minSize, maxSize, orderBy);
    }

    @Override
    public MetaData getDataDetails(String metaDataId) {
        return this.baseMapper.getDataDetails(metaDataId);
    }

    @Override
    public IPage<MetaData> getUserDataList(Page<MetaData> page, String address, String identityId) {
        return this.baseMapper.getUserDataList(page, address, identityId);
    }

    @Override
    public int countOfData() {
        return this.baseMapper.countOfData();
    }

    @Override
    public MetaData statisticsOfGlobal() {
        return this.baseMapper.statisticsOfGlobal();
    }
}
