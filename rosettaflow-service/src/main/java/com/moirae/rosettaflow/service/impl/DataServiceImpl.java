package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.common.enums.MetaDataStateEnum;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.manager.MetaDataAuthManager;
import com.moirae.rosettaflow.manager.MetaDataColumnManager;
import com.moirae.rosettaflow.manager.MetaDataManager;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

    @Resource
    private MetaDataManager metaDataManager;
    @Resource
    private MetaDataColumnManager metaDataColumnManager;
    @Resource
    private MetaDataAuthManager metaDataAuthManager;
    @Resource
    private CommonService commonService;

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

    @Override
    public List<MetaDataColumn> listMetaDataColumnAll(String metaDataId) {
        LambdaQueryWrapper<MetaDataColumn> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataColumn::getMetaDataId, metaDataId);
        wrapper.orderByAsc(MetaDataColumn::getColumnIdx);
        return metaDataColumnManager.list(wrapper);
    }

    @Override
    public MetaDataColumn getByKey(String metaDataId, Integer columnIdx) {
        LambdaQueryWrapper<MetaDataColumn> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaDataColumn::getMetaDataId, metaDataId);
        queryWrapper.eq(MetaDataColumn::getColumnIdx, columnIdx);
        return metaDataColumnManager.getOne(queryWrapper);
    }

    @Override
    @Transactional
    public void batchReplaceAuth(List<MetaDataAuth> metaDataAuthList) {
        metaDataAuthManager.saveOrUpdateBatch(metaDataAuthList);
    }

    @Override
    public IPage<MetaDataDto> list(Long current, Long size, String dataName) {
        Page<MetaDataDto> page = new Page<>(current, size);
        UserDto userDto = commonService.getCurrentUserOrNull();
        return metaDataManager.listByNameAndAuthAddress(page, dataName, userDto == null ? null : userDto.getAddress());
    }

    @Override
    public IPage<MetaDataDto> listMetaDataAuth(Long current, Long size, String dataName) {
        Page<MetaDataDto> page = new Page<>(current, size);
        UserDto userDto = commonService.getCurrentUser();
        return metaDataManager.listMetaDataAuth(page, dataName, userDto.getAddress());
    }

    @Override
    public MetaDataDto getMetaDataAuthDetails(String metaDataAuthId) {
        return metaDataManager.getMetaDataAuthDetails(metaDataAuthId);
    }
}
