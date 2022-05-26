package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.MetaDataManager;
import com.datum.platform.mapper.MetaDataMapper;
import com.datum.platform.mapper.domain.MetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public IPage<MetaData> getUserDataList(Page<MetaData> page, String address, String identityId, String keyword) {
        return this.baseMapper.getUserDataList(page, address, identityId, keyword);
    }

    @Override
    public MetaData statisticsOfGlobal() {
        return this.baseMapper.statisticsOfGlobal();
    }

    @Override
    public List<MetaData> listDataByTokenAddress(String tokenAddress) {
        LambdaQueryWrapper<MetaData> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaData::getTokenAddress, tokenAddress);
        return list(wrapper);
    }
}
