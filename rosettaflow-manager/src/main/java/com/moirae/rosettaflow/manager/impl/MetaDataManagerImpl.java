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
}
