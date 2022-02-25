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
import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.MetaDataDtoOld;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.mapper.MetaDataOldMapper;
import com.moirae.rosettaflow.mapper.domain.MetaDataOld;
import com.moirae.rosettaflow.mapper.domain.UserMetaData;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.IMetaDataOldService;
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
public class MetaDataOldServiceImpl extends ServiceImpl<MetaDataOldMapper, MetaDataOld> implements IMetaDataOldService {

    @Resource
    private IUserMetaDataService userMetaDataService;

    @Resource
    private CommonService commonService;

    @Override
    public IPage<MetaDataDtoOld> list(Long current, Long size, String dataName) {
        Page<MetaDataOld> page = new Page<>(current, size);
        LambdaQueryWrapper<MetaDataOld> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataOld::getDataStatus, MetaDataStateEnum.MetaDataState_Released.getValue());
        wrapper.eq(MetaDataOld::getStatus, StatusEnum.VALID.getValue());
        if (StrUtil.isNotBlank(dataName)) {
            wrapper.like(MetaDataOld::getDataName, dataName);
        }
        this.page(page, wrapper);
        //登录时，查询数据授权状态
        List<UserMetaData> metaDataWithAuthList = new ArrayList<>();
        if (!Objects.isNull(UserContext.get()) && StrUtil.isNotEmpty(UserContext.get().getAddress())) {
            Object[] metaDataIdArr = page.getRecords().stream().map(MetaDataOld::getMetaDataId).distinct().toArray();
            if (metaDataIdArr.length > 0) {
                metaDataWithAuthList = userMetaDataService.getCurrentUserMetaDataByMetaDataIdArr(metaDataIdArr);
            }
        }
        return this.convertToPageDto(page, metaDataWithAuthList);
    }

    @Override
    public MetaDataDtoOld detail(Long metaDataPkId, Long userMetaDataId) {
        // 查询元数据
        MetaDataOld metaData = this.getMetaDataById(metaDataPkId);
        if (Objects.isNull(metaData)) {
            log.error("query metaData fail by id:{}", metaDataPkId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_NOT_EXIST.getMsg());
        }
        MetaDataDtoOld metaDataDto = new MetaDataDtoOld();
        BeanCopierUtils.copy(metaData, metaDataDto);
        // 用户没有登录，不处理授权信息
        if (null == userMetaDataId ||  userMetaDataId == 0) {
            metaDataDto.setAuthType(AuthTypeEnum.UNKNOWN.getValue());
            return metaDataDto;
        }
        // 用户登录成功--查询用户授权数据
        UserMetaData userMetaData = userMetaDataService.getById(userMetaDataId);
        if(Objects.isNull(userMetaData)){
            log.error("query userMetaData fail by id:{}", userMetaDataId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_NOT_EXIST.getMsg());
        }
        metaDataDto.setAuthBeginTime(userMetaData.getAuthBeginTime());
        metaDataDto.setAuthEndTime(userMetaData.getAuthEndTime());
        metaDataDto.setApplyTime(userMetaData.getApplyTime());
        metaDataDto.setAuditTime(userMetaData.getAuditTime());
        metaDataDto.setAuthValue(userMetaData.getAuthValue());
        metaDataDto.setExpire(userMetaData.getExpire());
        metaDataDto.setAuthMetadataState(userMetaData.getAuthMetadataState());
        metaDataDto.setUsedTimes(userMetaData.getUsedTimes());
        metaDataDto.setAuthStatus(this.dealAuthStatus(userMetaData.getAuthStatus(), userMetaData.getAuthMetadataState()));
        //授权类型及授权值
        String authTime = "";
        metaDataDto.setAuthType(userMetaData.getAuthType());
        if(!Objects.isNull(userMetaData.getAuthBeginTime()) && !Objects.isNull(userMetaData.getAuthEndTime())){
            SimpleDateFormat dateFormat = new SimpleDateFormat(SysConstant.DEFAULT_TIME_PATTERN);
            dateFormat.setTimeZone(TimeZone.getTimeZone(SysConstant.DEFAULT_TIMEZONE));
            authTime = dateFormat.format(userMetaData.getAuthBeginTime()) + "~" + dateFormat.format(userMetaData.getAuthEndTime());
        }
        metaDataDto.setAuthValueStr(userMetaData.getAuthType() == AuthTypeEnum.NUMBER.getValue() ? String.valueOf(userMetaData.getAuthValue()) :  authTime);
        return metaDataDto;
    }

    @Override
    public MetaDataOld getMetaDataById(Long id){
        LambdaQueryWrapper<MetaDataOld> metaDataLambdaQueryWrapper = Wrappers.lambdaQuery();
        metaDataLambdaQueryWrapper.eq(MetaDataOld::getId, id);
        metaDataLambdaQueryWrapper.eq(MetaDataOld::getStatus,StatusEnum.VALID.getValue());
        return this.getOne(metaDataLambdaQueryWrapper);
    }

    @Override
    public List<MetaDataDtoOld> getAllAuthTables(String identityId) {
        UserDto userDto = commonService.getCurrentUser();
        String address = userDto.getAddress();
        if (!StrUtil.startWith(address, AddressChangeUtils.HRP_ETH)) {
            address = AddressChangeUtils.convert0xAddress(address);
        }

        return this.baseMapper.getAllAuthTables(identityId, address);
    }

    IPage<MetaDataDtoOld> convertToPageDto(Page<MetaDataOld> page, List<UserMetaData> metaDataWithAuthList) {
        List<MetaDataDtoOld> records = new ArrayList<>();
        Map<String, Byte> authStatusMap = new HashMap<>(metaDataWithAuthList.size());
        Map<String, Byte> authMetaValidMap = new HashMap<>(metaDataWithAuthList.size());
        Map<String, Long> authUserMetaDataPkId = new HashMap<>(metaDataWithAuthList.size());
        for (UserMetaData dataAuth : metaDataWithAuthList) {
            authStatusMap.put(dataAuth.getMetaDataId(), dataAuth.getAuthStatus());
            authMetaValidMap.put(dataAuth.getMetaDataId(), dataAuth.getAuthMetadataState());
            authUserMetaDataPkId.put(dataAuth.getMetaDataId(), dataAuth.getId());
        }
        page.getRecords().forEach(r -> {
            MetaDataDtoOld m = new MetaDataDtoOld();
            BeanCopierUtils.copy(r, m);
            m.setAuthStatus(authStatusMap.containsKey(r.getMetaDataId()) ? authStatusMap.get(r.getMetaDataId()) : UserMetaDataAuditEnum.AUDIT_UNKNOWN.getValue());
            m.setAuthMetadataState(authMetaValidMap.containsKey(r.getMetaDataId()) ? authMetaValidMap.get(r.getMetaDataId()) : UserMetaDataAuthorithStateEnum.UNKNOWN.getValue());
            m.setUserMateDataId(authUserMetaDataPkId.get(r.getMetaDataId()));
            records.add(m);
        });

        IPage<MetaDataDtoOld> pageDto = new Page<>();
        pageDto.setCurrent(page.getCurrent());
        pageDto.setRecords(records);
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotal());
        return pageDto;
    }

    @Override
    public Byte dealAuthStatus(Byte authStatus, Byte authMetadataState) {
        // 已撤销
        if (authMetadataState == UserMetaDataAuthorithStateEnum.REVOKED.getValue()) {
            return AuthStatusShowEnum.CANCELED.getValue();
        }
        // 已失效
        if (authMetadataState == UserMetaDataAuthorithStateEnum.INVALID.getValue()) {
            return AuthStatusShowEnum.INVALID.getValue();
        }
        // 未知
        if(null == authStatus) {
            return AuthStatusShowEnum.UNKNOWN.getValue();
        }
        return authStatus;
    }
}
