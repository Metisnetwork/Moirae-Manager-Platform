package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.DataOrderByEnum;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.manager.MetaDataManager;
import com.moirae.rosettaflow.mapper.MetaDataMapper;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.enums.MetaDataFileTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MetaDataManagerImpl extends ServiceImpl<MetaDataMapper, MetaData> implements MetaDataManager {

    @Override
    public IPage<MetaDataDto> listDataFileByIdentityId(Page<OrganizationDto> page, String identityId) {
        return this.baseMapper.listDataFileByIdentityId(identityId, page);
    }

    @Override
    public MetaDataDto getDataFile(String metaDataId) {
        return this.baseMapper.getDataFile(metaDataId);
    }

    @Override
    public IPage<MetaDataDto> listByNameAndAuthAddress(Page<MetaDataDto> page, String dataName, String address) {
        return this.baseMapper.listByNameAndAuthAddress(page, dataName, address);
    }

    @Override
    public IPage<MetaDataDto> listMetaDataAuth(Page<MetaDataDto> page, String dataName, String address) {
        return this.baseMapper.listMetaDataAuth(page, dataName, address);
    }

    @Override
    public MetaDataDto getMetaDataAuthDetails(String metaDataAuthId) {
        return this.baseMapper.getMetaDataAuthDetails(metaDataAuthId);
    }

    @Override
    public List<MetaDataDto> getOrgChooseListByMetaDataAuth(String address) {
        return this.baseMapper.getOrgChooseListByMetaDataAuth(address);
    }

    @Override
    public List<MetaDataDto> getMetaDataByChoose(String address, String identityId) {
        return this.baseMapper.getMetaDataByChoose(address, identityId);
    }

    @Override
    public IPage<MetaData> getDataListByOrg(Page<MetaData> page, String identityId) {
        return this.baseMapper.getDataListByOrg(page, identityId);
    }

    @Override
    public IPage<MetaData> getDataList(Page<MetaData> page, String keyword, String industry, MetaDataFileTypeEnum fileType, Long minSize, Long maxSize, DataOrderByEnum orderBy) {
        return this.baseMapper.getDataList(page, keyword, industry, fileType, minSize, maxSize, orderBy.getSqlValue());
    }

    @Override
    public MetaData getDataDetails(String metaDataId) {
        return this.baseMapper.getDataDetails(metaDataId);
    }

    @Override
    public IPage<MetaData> getUserDataList(Page<MetaData> page, String address) {
        return this.baseMapper.getUserDataList(page, address);
    }
}
