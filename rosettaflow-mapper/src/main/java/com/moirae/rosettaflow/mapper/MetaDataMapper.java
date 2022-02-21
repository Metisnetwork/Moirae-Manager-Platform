package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;

/**
 * @author admin
 */
public interface MetaDataMapper extends BaseMapper<MetaData> {

    IPage<MetaDataDto> listDataFileByIdentityId(String identityId, Page<OrganizationDto> page);

    MetaDataDto getDataFile(String metaDataId);
}
