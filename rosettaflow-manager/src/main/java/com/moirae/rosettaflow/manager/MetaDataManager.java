package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;

import java.util.List;

public interface MetaDataManager extends IService<MetaData> {

    IPage<MetaDataDto> listDataFileByIdentityId(Page<OrganizationDto> page, String identityId);

    MetaDataDto getDataFile(String metaDataId);

    IPage<MetaDataDto> listByNameAndAuthAddress(Page<MetaDataDto> page, String dataName, String address);

    IPage<MetaDataDto> listMetaDataAuth(Page<MetaDataDto> page, String dataName, String address);

    MetaDataDto getMetaDataAuthDetails(String metaDataAuthId);

    List<MetaDataDto> getOrgChooseListByMetaDataAuth(String address);

    List<MetaDataDto> getMetaDataByChoose(String address, String identityId);
}
