package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.MetaData;

public interface MetaDataManager extends IService<MetaData> {

    IPage<MetaData> getDataListByOrg(Page<MetaData> page, String identityId);

    IPage<MetaData> getDataList(Page<MetaData> page, String keyword, String industry, Integer fileType, Long minSize, Long maxSize, String orderBy);

    MetaData getDataDetails(String metaDataId);

    IPage<MetaData> getUserDataList(Page<MetaData> page, String address, String identityId);

    int countOfData();

    MetaData statisticsOfGlobal();
}
