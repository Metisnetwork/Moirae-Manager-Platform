package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;

public interface MetaDataManager extends IService<MetaData> {

    IPage<MetaDataDto> listDataFileByIdentityId(Page<OrganizationDto> page, String identityId);

    MetaDataDto getDataFile(String metaDataId);
}
