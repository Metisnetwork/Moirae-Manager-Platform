package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.common.enums.DataOrderByEnum;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.manager.MetaDataColumnManager;
import com.moirae.rosettaflow.manager.MetaDataManager;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import com.moirae.rosettaflow.mapper.enums.MetaDataFileTypeEnum;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.moirae.rosettaflow.common.enums.RespCodeEnum.BIZ_EXCEPTION;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

    @Resource
    private MetaDataManager metaDataManager;
    @Resource
    private MetaDataColumnManager metaDataColumnManager;

    @Override
    public int getDataCount() {
        return metaDataManager.getDataCount();
    }

    @Override
    public IPage<MetaData> getDataListByOrg(Long current, Long size, String identityId) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getDataListByOrg(page, identityId);
    }

    @Override
    public IPage<MetaData> getDataList(Long current, Long size, String keyword, String industry, MetaDataFileTypeEnum fileType, Long minSize, Long maxSize, DataOrderByEnum orderBy) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getDataList(page, keyword, industry, fileType, minSize, maxSize, orderBy);
    }

    @Override
    public MetaData getDataDetails(String metaDataId) {
        MetaData metaData = metaDataManager.getDataDetails(metaDataId);
        List<MetaDataColumn> columnList = metaDataColumnManager.getList(metaDataId);
        metaData.setColumnsList(columnList);
        return metaData;
    }

    @Override
    public IPage<MetaData> getUserDataList(Long current, Long size) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getUserDataList(page, UserContext.getCurrentUser().getAddress());
    }

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
    public Map<String, MetaData> getMetaDataId2metaDataMap(Set<String> metaDataId) {
        return metaDataManager.listByIds(metaDataId).stream().collect(Collectors.toMap(MetaData::getMetaDataId, item -> item));
    }


    @Override
    public MetaDataColumn getByKey(String metaDataId, Integer columnIdx) {
        LambdaQueryWrapper<MetaDataColumn> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaDataColumn::getMetaDataId, metaDataId);
        queryWrapper.eq(MetaDataColumn::getColumnIdx, columnIdx);
        return metaDataColumnManager.getOne(queryWrapper);
    }


    @Override
    public void checkMetaDataEffective(String metaDataId) {
        LambdaQueryWrapper<MetaData> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaData::getMetaDataId, metaDataId);
        queryWrapper.eq(MetaData::getStatus, com.moirae.rosettaflow.mapper.enums.MetaDataStatusEnum.PUBLISHED);
        int count = metaDataManager.count(queryWrapper);
        if(count != 1){
            //无效元数据
            throw new BusinessException(BIZ_EXCEPTION, StrUtil.format(ErrorMsg.METADATA_UNAVAILABLE_FORMAT.getMsg(), metaDataId));
        }
    }

    @Override
    //TODO
    public void checkMetaDataAuthListEffective(String address, Set<String> metaDataIdList) {
//        LambdaQueryWrapper<MetaDataAuth> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper.eq(MetaDataAuth::getAuthStatus, MetaDataAuthStatusEnum.PUBLISHED);
//        queryWrapper.eq(MetaDataAuth::getUserId, address);
//        queryWrapper.in(MetaDataAuth::getMetaDataId, metaDataIdList);
//        int count = metaDataAuthManager.count(queryWrapper);
//        if(count != metaDataIdList.size()){
//            //无效元数据
//            log.error("有授权数据已过期，请检查, metaDataIdList:{}", metaDataIdList);
//            throw new AppException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_DATA_EXPIRE.getMsg());
//        }
    }
}
