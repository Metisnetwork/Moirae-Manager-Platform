package com.platon.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.*;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.MetaDataDto;
import com.platon.rosettaflow.mapper.MetaDataMapper;
import com.platon.rosettaflow.mapper.domain.MetaData;
import com.platon.rosettaflow.mapper.domain.UserMetaData;
import com.platon.rosettaflow.service.IMetaDataService;
import com.platon.rosettaflow.service.IUserMetaDataService;
import com.platon.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
public class MetaDataServiceImpl extends ServiceImpl<MetaDataMapper, MetaData> implements IMetaDataService {

    @Resource
    private IUserMetaDataService userMetaDataService;

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    @Override
    public IPage<MetaDataDto> list(Long current, Long size, String dataName) {
        Page<MetaData> page = new Page<>(current, size);
        LambdaQueryWrapper<MetaData> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaData::getDataStatus, MetaDataStateEnum.MetaDataState_Released.getValue());
        wrapper.eq(MetaData::getStatus, StatusEnum.VALID.getValue());
        if (StrUtil.isNotBlank(dataName)) {
            wrapper.like(MetaData::getDataName, dataName);
        }
        this.page(page, wrapper);
        //登录时，查询数据授权状态
        List<UserMetaData> metaDataWithAuthList = new ArrayList<>();
        if (!Objects.isNull(UserContext.get()) && StrUtil.isNotEmpty(UserContext.get().getAddress())) {
            metaDataWithAuthList = userMetaDataService.getCurrentUserMetaDataByMetaDataIdArr(page.getRecords().stream().map(MetaData::getMetaDataId).distinct().toArray());
        }
        return this.convertToPageDto(page, metaDataWithAuthList);
    }

    @Override
    public MetaDataDto detail(Long id) {
        MetaData metaData = this.getById(id);
        if (Objects.isNull(metaData)) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_NOT_EXIST.getMsg());
        }
        MetaDataDto metaDataDto = new MetaDataDto();
        BeanCopierUtils.copy(metaData, metaDataDto);
        UserMetaData userMetaData = userMetaDataService.getCurrentUserMetaDataByMetaDataId(metaData.getMetaDataId());
        if (null != userMetaData) {
            metaDataDto.setAuthType(userMetaData.getAuthType());
        } else {
            metaDataDto.setAuthType((byte) MetaDataUsageEnum.USAGE_UNKNOWN.getValue());
        }
        return metaDataDto;
    }

    @Override
    public List<MetaDataDto> getAllAuthTables(String identityId) {
        return this.baseMapper.getAllAuthTables(identityId);
    }

    @Override
    public void batchInsert(List<MetaData> metaDataList) {
        this.baseMapper.batchInsert(metaDataList);
    }

    IPage<MetaDataDto> convertToPageDto(Page<MetaData> page, List<UserMetaData> metaDataWithAuthList) {
        List<MetaDataDto> records = new ArrayList<>();
        Map<String, Byte> authMap = new HashMap<>(metaDataWithAuthList.size());
        for (UserMetaData dataAuth : metaDataWithAuthList) {
            authMap.put(dataAuth.getMetaDataId(), dataAuth.getAuthStatus());
        }
        page.getRecords().forEach(r -> {
            MetaDataDto m = new MetaDataDto();
            BeanCopierUtils.copy(r, m);
            m.setAuthStatus(authMap.containsKey(r.getMetaDataId()) ? authMap.get(r.getMetaDataId()) : UserMetaDataAuditEnum.AUDIT_UNKNOWN.getValue());
            records.add(m);
        });

        IPage<MetaDataDto> pageDto = new Page<>();
        pageDto.setCurrent(page.getCurrent());
        pageDto.setRecords(records);
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotal());
        return pageDto;
    }
}
