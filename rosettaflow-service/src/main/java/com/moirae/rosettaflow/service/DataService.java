package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.MetaDataColumnDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataAuth;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthTypeEnum;

import java.util.Date;
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

    IPage<MetaDataDto> list(Long current, Long size, String dataName);

    IPage<MetaDataDto> listMetaDataAuth(Long current, Long size, String dataName);

    MetaDataDto getMetaDataAuthDetails(String metaDataAuthId);

    List<MetaDataDto> getOrgChooseListByMetaDataAuth();

    List<MetaDataDto> getMetaDataByChoose(String identityId);

    void apply(String metaDataId, MetaDataAuthTypeEnum authType, Date startAt, Date endAt, Integer times, String sign);

    void revoke(String metadataAuthId, String sign);

    void checkMetaDataEffective(String metaDataId);

    void checkMetaDataAuthListEffective(String address, Set<String> metaDataIdList);
}
