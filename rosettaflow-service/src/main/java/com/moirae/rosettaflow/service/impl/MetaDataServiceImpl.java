package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.mapper.MetaDataMapper;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.UserMetaData;
import com.moirae.rosettaflow.service.IMetaDataService;
import com.moirae.rosettaflow.service.IUserMetaDataService;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

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
            Object[] metaDataIdArr = page.getRecords().stream().map(MetaData::getMetaDataId).distinct().toArray();
            if (metaDataIdArr.length > 0) {
                metaDataWithAuthList = userMetaDataService.getCurrentUserMetaDataByMetaDataIdArr(metaDataIdArr);
            }
        }
        return this.convertToPageDto(page, metaDataWithAuthList);
    }

    @Override
    public MetaDataDto detail(String metaDataId) {
        MetaData metaData = this.getMetaDataBymetaId(metaDataId);
        if (Objects.isNull(metaData)) {
            log.error("query metaData fail by metaDataId:{}", metaDataId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_NOT_EXIST.getMsg());
        }
        MetaDataDto metaDataDto = new MetaDataDto();
        BeanCopierUtils.copy(metaData, metaDataDto);
        UserMetaData userMetaData = userMetaDataService.getCurrentUserMetaDataByMetaDataId(metaData.getMetaDataId());
        if (null != userMetaData) {
            metaDataDto.setAuthType(userMetaData.getAuthType());
            metaDataDto.setAuthBeginTime(userMetaData.getAuthBeginTime());
            metaDataDto.setAuthEndTime(userMetaData.getAuthEndTime());
            metaDataDto.setAuthValue(userMetaData.getAuthValue());
            //授权值
            String authTime = "";
            if(!Objects.isNull(userMetaData.getAuthBeginTime()) && !Objects.isNull(userMetaData.getAuthEndTime())){
                SimpleDateFormat dateFormat = new SimpleDateFormat(SysConstant.DEFAULT_TIME_PATTERN);
                dateFormat.setTimeZone(TimeZone.getTimeZone(SysConstant.DEFAULT_TIMEZONE));
                authTime = dateFormat.format(userMetaData.getAuthBeginTime()) + "~" + dateFormat.format(userMetaData.getAuthEndTime());
            }
            metaDataDto.setAuthValueStr(userMetaData.getAuthType() == AuthTypeEnum.NUMBER.getValue() ? String.valueOf(userMetaData.getAuthValue()) :  authTime);

        } else {
            metaDataDto.setAuthType((byte) MetaDataUsageEnum.USAGE_UNKNOWN.getValue());
        }

        return metaDataDto;
    }

    @Override
    public MetaData getMetaDataBymetaId(String metaDataId) {
        LambdaQueryWrapper<MetaData> metaDataLambdaQueryWrapper = Wrappers.lambdaQuery();
        metaDataLambdaQueryWrapper.eq(MetaData::getMetaDataId,metaDataId);
        metaDataLambdaQueryWrapper.eq(MetaData::getStatus,StatusEnum.VALID.getValue());
        return this.getOne(metaDataLambdaQueryWrapper);
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
        Map<String, Byte> authStatusMap = new HashMap<>(metaDataWithAuthList.size());
        Map<String, Byte> authMetaValidMap = new HashMap<>(metaDataWithAuthList.size());
        for (UserMetaData dataAuth : metaDataWithAuthList) {
            authStatusMap.put(dataAuth.getMetaDataId(), dataAuth.getAuthStatus());
            authMetaValidMap.put(dataAuth.getMetaDataId(), dataAuth.getAuthMetadataState());
        }
        page.getRecords().forEach(r -> {
            MetaDataDto m = new MetaDataDto();
            BeanCopierUtils.copy(r, m);
            m.setAuthStatus(authStatusMap.containsKey(r.getMetaDataId()) ? authStatusMap.get(r.getMetaDataId()) : UserMetaDataAuditEnum.AUDIT_UNKNOWN.getValue());
            m.setAuthMetadataState(authMetaValidMap.containsKey(r.getMetaDataId()) ? authMetaValidMap.get(r.getMetaDataId()) : UserMetaDataAuthorithStateEnum.UNKNOWN.getValue());
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
