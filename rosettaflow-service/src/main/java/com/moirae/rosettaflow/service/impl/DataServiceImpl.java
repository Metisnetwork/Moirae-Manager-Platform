package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.manager.MetaDataColumnManager;
import com.moirae.rosettaflow.manager.MetaDataManager;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import com.moirae.rosettaflow.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

    @Resource
    private MetaDataManager metaDataManager;
    @Resource
    private MetaDataColumnManager metaDataColumnManager;

    @Override
    @Transactional
    public void batchReplace(List<MetaData> metaDataList, List<MetaDataColumn> metaDataColumnList) {
        metaDataManager.saveOrUpdateBatch(metaDataList);
        LambdaQueryWrapper<MetaDataColumn> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MetaDataColumn::getMetaDataId, metaDataList.stream().map(MetaData::getMetaDataId).collect(Collectors.toSet()));
        metaDataColumnManager.remove(wrapper);
        metaDataColumnManager.saveBatch(metaDataColumnList);
    }

    @Override
    public IPage<MetaDataDto> listDataFileByIdentityId(Long current, Long size, String identityId) {
        Page<OrganizationDto> page = new Page<>(current, size);
        return metaDataManager.listDataFileByIdentityId(page, identityId);
    }

    @Override
    public MetaDataDto getDataFile(String metaDataId) {
        return metaDataManager.getDataFile(metaDataId);
    }

    @Override
    public IPage<MetaDataColumn> listMetaDataColumn(Long current, Long size, String metaDataId) {
        Page<MetaDataColumn> page = new Page<>(current, size);
        LambdaQueryWrapper<MetaDataColumn> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataColumn::getMetaDataId, metaDataId);
        wrapper.orderByAsc(MetaDataColumn::getColumnIdx);
        return metaDataColumnManager.page(page, wrapper);
    }

    @Override
    public Map<String, MetaData> getMetaDataId2metaDataMap(Set<String> metaDataId) {
        return metaDataManager.listByIds(metaDataId).stream().collect(Collectors.toMap(MetaData::getMetaDataId, item -> item));
    }
}
