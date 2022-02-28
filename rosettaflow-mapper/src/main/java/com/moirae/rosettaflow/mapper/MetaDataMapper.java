package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;

import java.util.List;

/**
 * @author admin
 */
public interface MetaDataMapper extends BaseMapper<MetaData> {

    IPage<MetaDataDto> listDataFileByIdentityId(String identityId, Page<OrganizationDto> page);

    MetaDataDto getDataFile(String metaDataId);

    IPage<MetaDataDto> listByNameAndAuthAddress(Page<MetaDataDto> page, String dataName, String address);

    IPage<MetaDataDto> listMetaDataAuth(Page<MetaDataDto> page, String dataName, String address);

    MetaDataDto getMetaDataAuthDetails(String metaDataAuthId);

    List<MetaDataDto> getOrgChooseListByMetaDataAuth(String address);

    List<MetaDataDto> getMetaDataByChoose(String address, String identityId);
}
