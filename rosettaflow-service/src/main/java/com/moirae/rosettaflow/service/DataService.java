package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.MetaDataColumnDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataAuth;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import com.moirae.rosettaflow.dto.MetaDataDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataService {

    void batchReplace(List<MetaData> metaDataList, List<MetaDataColumn> metaDataColumnList);

    /**
     * 查询元数据列表
     *
     * @param current
     * @param size
     * @param identityId
     * @return
     */
    IPage<MetaDataDto> listDataFileByIdentityId(Long current, Long size, String identityId);

    /**
     * 查询元数据信息
     *
     * @param metaDataId
     * @return
     */
    MetaDataDto getDataFile(String metaDataId);

    IPage<MetaDataColumn> listMetaDataColumn(Long current, Long size, String metaDataId);

    Map<String, MetaData> getMetaDataId2metaDataMap(Set<String> metaDataId);

    List<MetaDataColumn> listMetaDataColumnAll(String metaDataId);

    MetaDataColumn getByKey(String metaDataId, Integer columnIdx);

    void batchReplaceAuth(List<MetaDataAuth> metaDataAuthList);
}
