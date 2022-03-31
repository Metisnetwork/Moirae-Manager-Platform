package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.common.enums.DataOrderByEnum;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.enums.MetaDataFileTypeEnum;

import java.util.List;

public interface MetaDataManager extends IService<MetaData> {

    IPage<MetaDataDto> listDataFileByIdentityId(Page<OrganizationDto> page, String identityId);

    MetaDataDto getDataFile(String metaDataId);

    IPage<MetaDataDto> listByNameAndAuthAddress(Page<MetaDataDto> page, String dataName, String address);

    IPage<MetaDataDto> listMetaDataAuth(Page<MetaDataDto> page, String dataName, String address);

    MetaDataDto getMetaDataAuthDetails(String metaDataAuthId);

    List<MetaDataDto> getOrgChooseListByMetaDataAuth(String address);

    List<MetaDataDto> getMetaDataByChoose(String address, String identityId);

    IPage<MetaData> getDataListByOrg(Page<MetaData> page, String identityId);

    IPage<MetaData> getDataList(Page<MetaData> page, String keyword, String industry, MetaDataFileTypeEnum fileType, Long minSize, Long maxSize, DataOrderByEnum orderBy);

    MetaData getDataDetails(String metaDataId);

    IPage<MetaData> getUserDataList(Page<MetaData> page, String address);

    int getDataCount();
}
