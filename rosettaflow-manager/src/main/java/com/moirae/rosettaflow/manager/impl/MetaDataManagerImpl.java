package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.manager.MetaDataManager;
import com.moirae.rosettaflow.mapper.MetaDataMapper;
import com.moirae.rosettaflow.mapper.domain.MetaData;
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
}
