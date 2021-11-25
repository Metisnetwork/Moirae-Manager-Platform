package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.dto.UserMetaDataDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataUsageRuleDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.RevokeMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.RevokeMetadataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import com.moirae.rosettaflow.mapper.UserMetaDataMapper;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.UserMetaData;
import com.moirae.rosettaflow.service.IMetaDataService;
import com.moirae.rosettaflow.service.IUserMetaDataService;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
public class UserMetaDataServiceImpl extends ServiceImpl<UserMetaDataMapper, UserMetaData> implements IUserMetaDataService {

    @Resource
    private IMetaDataService metaDataService;

    @Resource
    private GrpcAuthService grpcAuthService;

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    @Override
    public IPage<UserMetaDataDto> list(Long current, Long size, String dataName) {
        Page<UserMetaData> page = new Page<>(current, size);
        if (Objects.isNull(UserContext.get()) || null == UserContext.get().getAddress()) {
            log.error(ErrorMsg.USER_UN_LOGIN.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_UN_LOGIN.getMsg());
        }
        return this.baseMapper.listByOwner(page, UserContext.get().getAddress(), dataName);
    }

    @Override
    public void auth(UserMetaDataDto userMetaDataDto) {
        MetaData metaData = metaDataService.getById(userMetaDataDto.getId());
        if (null == metaData) {
            log.error("query meta data not exist, id:{}", userMetaDataDto.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_NOT_EXIST.getMsg());
        }
        if (userMetaDataDto.getAuthType() == MetaDataUsageEnum.TIMES.getValue()) {
            if (null == userMetaDataDto.getAuthValue() || userMetaDataDto.getAuthValue() < 1) {
                log.error(ErrorMsg.METADATA_AUTH_TIMES_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_TIMES_ERROR.getMsg());
            }
        } else {
            if (null == userMetaDataDto.getAuthEndTime() || null == userMetaDataDto.getAuthBeginTime() ||
                DateUtil.compare(userMetaDataDto.getAuthEndTime(), new Date()) < 0 || userMetaDataDto.getAuthEndTime().before(userMetaDataDto.getAuthBeginTime())) {
                log.error(ErrorMsg.METADATA_AUTH_TIME_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_TIME_ERROR.getMsg());
            }
        }
        // 检验授权数据是否有效
        checkMetaDataAuthValid(metaData.getMetaDataId());

        ApplyMetaDataAuthorityRequestDto applyDto = new ApplyMetaDataAuthorityRequestDto();
        applyDto.setUser(userMetaDataDto.getAddress());
        applyDto.setUserType(UserTypeEnum.checkUserType(userMetaDataDto.getAddress()));

        MetaDataAuthorityDto metaDataAuthorityDto = new MetaDataAuthorityDto();
        //元数据所属组织
        NodeIdentityDto owner = new NodeIdentityDto();
        owner.setNodeName(metaData.getIdentityName());
        owner.setNodeId(metaData.getNodeId());
        owner.setIdentityId(metaData.getIdentityId());
        metaDataAuthorityDto.setOwner(owner);

        //元数据id
        metaDataAuthorityDto.setMetaDataId(metaData.getMetaDataId());

        //元数据怎么使用
        MetaDataUsageRuleDto metaDataUsageDto = new MetaDataUsageRuleDto();
        metaDataUsageDto.setUseType((int) userMetaDataDto.getAuthType());
        if (userMetaDataDto.getAuthType() == MetaDataUsageEnum.PERIOD.getValue()) {
            metaDataUsageDto.setStartAt(userMetaDataDto.getAuthBeginTime().getTime());
            metaDataUsageDto.setEndAt(userMetaDataDto.getAuthEndTime().getTime());
        }
        metaDataUsageDto.setTimes(userMetaDataDto.getAuthValue());
        metaDataAuthorityDto.setMetaDataUsageDto(metaDataUsageDto);

        applyDto.setAuth(metaDataAuthorityDto);
        applyDto.setSign(userMetaDataDto.getSign());

        ApplyMetaDataAuthorityResponseDto responseDto = grpcAuthService.applyMetaDataAuthority(applyDto);
        if (responseDto.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE || StrUtil.isBlank(responseDto.getMetaDataAuthId())) {
            log.error("元数据授权申请,net处理失败，失败原因：{}", responseDto);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, responseDto.getMsg());
        }
        // 保存用户授权申请元数据, rpc接口调用失败时，不保存授权元数据
        this.saveUserMetaData(userMetaDataDto, metaData, responseDto.getMetaDataAuthId());
        log.info("元数据授权申请id为：{}", responseDto.getMetaDataAuthId());
    }

    @Override
    public void revoke(UserMetaDataDto userMetaDataDto) {
        //检查userMetaData是否存在
        LambdaQueryWrapper<UserMetaData> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserMetaData::getId, userMetaDataDto.getId());
        queryWrapper.eq(UserMetaData::getStatus, StatusEnum.VALID.getValue());
        UserMetaData userMetaData = this.getOne(queryWrapper);
       if (Objects.isNull(userMetaData)) {
           log.error("query userMetaData fail by id:{}", userMetaDataDto.getId());
           throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_NOT_EXIST.getMsg());
       }
        //撤销元数据授权
        RevokeMetaDataAuthorityRequestDto requestDto = new RevokeMetaDataAuthorityRequestDto();
        requestDto.setUser(userMetaDataDto.getAddress());
        requestDto.setMetadataAuthId(userMetaDataDto.getMetadataAuthId());
        requestDto.setSign(userMetaDataDto.getSign());
        requestDto.setUserType(UserTypeEnum.checkUserType(userMetaDataDto.getAddress()));
        RevokeMetadataAuthorityResponseDto responseDto = grpcAuthService.revokeMetadataAuthority(requestDto);
        if (responseDto.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("撤销元数据授权,net处理失败，失败原因：{}", responseDto);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, responseDto.getMsg());
        }
        //更新用户授权元数据，状态为已撤销数据授权
        LambdaUpdateWrapper<UserMetaData>  updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserMetaData::getId, userMetaDataDto.getId());
        updateWrapper.set(UserMetaData::getAuthMetadataState, UserMetaDataAuthorithStateEnum.REVOKED.getValue());
        boolean updateSuccess = this.update(updateWrapper);
        if (!updateSuccess) {
            log.error("更新用户授权数据信息的状态auth_metadata_state失败,id：{}", userMetaDataDto.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_AUTH_METADATA_STATE_UPDATE_FAIL.getMsg());
        }
    }

    /**
     * 保存用户授权申请元数据
     */
    private void saveUserMetaData(UserMetaDataDto userMetaDataDto, MetaData metaData, String metadataAuthId) {
        // 保存等待审核数据
        UserMetaData userMetaData = new UserMetaData();
        BeanCopierUtils.copy(userMetaDataDto, userMetaData);
        userMetaData.setMetaDataId(metaData.getMetaDataId());
        userMetaData.setIdentityName(metaData.getIdentityName());
        userMetaData.setAddress(AddressChangeUtils.convert0xAddress(userMetaDataDto.getAddress()));
        userMetaData.setApplyTime(new Date());
        userMetaData.setAuthStatus(UserMetaDataAuditEnum.AUDIT_PENDING.getValue());
        userMetaData.setAuthMetadataState(UserMetaDataAuthorithStateEnum.RELEASED.getValue());
        userMetaData.setMetadataAuthId(metadataAuthId);
        userMetaData.setId(null);
        if (!this.save(userMetaData)) {
            log.error("元数据授权申请, 保存等待审核元数据失败，userMetaData：{}", userMetaData);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_SAVE_ERROR.getMsg());
        }
        log.info("元数据授权申请, 保存等待审核元数据成功，userMetaData：{}", userMetaData);
    }

    @Override
    public List<UserMetaDataDto> getAllAuthOrganization() {
        UserDto userDto = UserContext.get();
        if (Objects.isNull(userDto)) {
            log.error(ErrorMsg.USER_UN_LOGIN.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_UN_LOGIN.getMsg());
        }
        String address = userDto.getAddress();
        if (!StrUtil.startWith(userDto.getAddress(), AddressChangeUtils.HRP_ETH)) {
            address = AddressChangeUtils.convert0xAddress(address);
        }
        return this.baseMapper.getUserMetaDataByAddress(address);
    }

    @Override
    public List<MetaDataDto> getAllAuthTables(String identityId) {
        return metaDataService.getAllAuthTables(identityId);
    }

    @Override
    public UserMetaData getCurrentUserMetaDataByMetaDataId(String metaDataId) {
        //用户没有登录不查询
        if (Objects.isNull(UserContext.get()) || null == UserContext.get().getAddress()) {
            return null;
        }
        LambdaQueryWrapper<UserMetaData> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserMetaData::getMetaDataId, metaDataId);
        wrapper.eq(UserMetaData::getAddress, UserContext.get().getAddress());
        wrapper.eq(UserMetaData::getAuthMetadataState, UserMetaDataAuthorithStateEnum.RELEASED.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public List<UserMetaData> getCurrentUserMetaDataByMetaDataIdArr(Object[] metaDataIdArr) {
        LambdaQueryWrapper<UserMetaData> wrapper = Wrappers.lambdaQuery();
        wrapper.in(UserMetaData::getMetaDataId, metaDataIdArr);
        wrapper.eq(UserMetaData::getAddress, UserContext.get().getAddress());
        wrapper.orderByAsc(UserMetaData :: getApplyTime);
        return this.list(wrapper);
    }

    @Override
    public int batchInsert(List<UserMetaData> userMetaDataList) {
        return this.baseMapper.batchInsert(userMetaDataList);
    }

    @Override
    public void batchUpdate(List<UserMetaData> userMetaDataList) {
        this.baseMapper.batchUpdate(userMetaDataList);
    }

    @Override
    public void updateTimesByMetaDataId(List<String> metaDataIdList, String address) {
        this.baseMapper.updateTimesByMetaDataId(metaDataIdList, address);
    }

    @Override
    public List<UserMetaData> getByMetaDataId(Set<String> metaDataIdList) {
        LambdaQueryWrapper<UserMetaData> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserMetaData::getAuthMetadataState, UserMetaDataAuthorithStateEnum.RELEASED.getValue());
        queryWrapper.eq(UserMetaData::getStatus, StatusEnum.VALID.getValue());
        queryWrapper.in(UserMetaData::getMetaDataId, metaDataIdList);
        return this.list(queryWrapper);
    }

    @Override
    public List<UserMetaData> getByAuthStatus(Byte authStatus) {
        LambdaQueryWrapper<UserMetaData> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserMetaData::getAuthStatus, authStatus);
        queryWrapper.eq(UserMetaData::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }

    /**
     *  检查授权数据是否有效
     * @param metaDataId ；元数据id
     */
    private void checkMetaDataAuthValid(String metaDataId){
        Map<String,Byte> metaDataAuthStatusMap = new HashMap<>(2);
        Map<String,Byte> authMetadataStateMap = new HashMap<>(2);
        List<UserMetaData> metaDataWithAuthList = this.getCurrentUserMetaDataByMetaDataIdArr(Collections.singletonList(metaDataId).toArray());
        // 首次授权没有授权历史数据，直接返回
        if (null == metaDataWithAuthList || metaDataWithAuthList.size() == 0) {
            return;
        }
        metaDataWithAuthList.forEach(userMetaData -> {
            metaDataAuthStatusMap.put(userMetaData.getMetaDataId(), userMetaData.getAuthStatus());
            authMetadataStateMap.put(userMetaData.getMetaDataId(), userMetaData.getAuthMetadataState());
        });

        //校验状态审核中
        if (metaDataAuthStatusMap.containsKey(metaDataId) && metaDataAuthStatusMap.get(metaDataId) == UserMetaDataAuditEnum.AUDIT_PENDING.getValue()) {
            log.error("Meta data auth audit pending,can not reapply,metaDataId:{}",metaDataId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_PENDING_ERROR.getMsg());
        }
        //校验状态审核通过
        if (metaDataAuthStatusMap.containsKey(metaDataId) && metaDataAuthStatusMap.get(metaDataId) == UserMetaDataAuditEnum.AUDIT_PASSED.getValue() &&
            authMetadataStateMap.containsKey(metaDataId) && authMetadataStateMap.get(metaDataId) == UserMetaDataAuthorithStateEnum.RELEASED.getValue()) {
            log.error("Meta data auth unexpired,can not reapply,metaDataId:{}",metaDataId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_UNEXPIRED_ERROR.getMsg());
        }

    }
}
