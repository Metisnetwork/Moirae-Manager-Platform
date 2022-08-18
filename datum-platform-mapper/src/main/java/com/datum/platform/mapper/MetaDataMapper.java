package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.MetaData;

import java.util.List;

/**
 * @author admin
 */
public interface MetaDataMapper extends BaseMapper<MetaData> {

    IPage<MetaData> getDataListByOrg(Page<MetaData> page, String identityId);

    IPage<MetaData> getDataList(Page<MetaData> page, String keyword, String industry, Integer fileType, Long minSize, Long maxSize, String orderBy);

    MetaData getDataDetails(String metaDataId);

    IPage<MetaData> getUserDataList(Page<MetaData> page, String address, String identityId, String keyword);

    IPage<MetaData> getUserAuthDataList(Page<MetaData> page, String address, String keyword);

    long sizeOfData();

    MetaData statisticsOfGlobal();

    List<String> listDataOrgIdByUser(String address, Integer algorithmType);
}
