package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataUsageRuleDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.RevokeMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.RevokeMetadataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import com.moirae.rosettaflow.manager.MetaDataAuthManager;
import com.moirae.rosettaflow.manager.MetaDataColumnManager;
import com.moirae.rosettaflow.manager.MetaDataManager;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthOptionEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthStatusEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthTypeEnum;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.moirae.rosettaflow.common.enums.RespCodeEnum.BIZ_EXCEPTION;

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
    @Resource
    private OrganizationService organizationService;
    @Resource
    private GrpcAuthService grpcAuthService;

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

    @Override
    public List<MetaDataDto> getOrgChooseListByMetaDataAuth() {
        UserDto userDto = commonService.getCurrentUser();
        return metaDataManager.getOrgChooseListByMetaDataAuth(userDto.getAddress());
    }

    @Override
    public List<MetaDataDto> getMetaDataByChoose(String identityId) {
        UserDto userDto = commonService.getCurrentUser();
        return metaDataManager.getMetaDataByChoose(userDto.getAddress(), identityId);
    }

    @Override
    public void apply(String metaDataId, MetaDataAuthTypeEnum authType, Date startAt, Date endAt, Integer times, String sign) {
        MetaData metaData = metaDataManager.getById(metaDataId);
        if (null == metaData) {
            log.error("query meta data not exist, id:{}", metaDataId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_NOT_EXIST.getMsg());
        }
        if(metaData.getStatus() != com.moirae.rosettaflow.mapper.enums.MetaDataStatusEnum.PUBLISHED){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_UNAVAILABLE.getMsg());
        }
        if (authType == MetaDataAuthTypeEnum.TIMES) {
            if (null == times || times < 1) {
                log.error(ErrorMsg.METADATA_AUTH_TIMES_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_TIMES_ERROR.getMsg());
            }
        } else {
            if (null == endAt
                    || null == startAt
                    || DateUtil.compare(endAt, new Date()) < 0
                    || endAt.before(startAt)) {
                log.error(ErrorMsg.METADATA_AUTH_TIME_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_TIME_ERROR.getMsg());
            }
        }
        // 检验授权数据是否有效
        UserDto userDto = commonService.getCurrentUser();
        LambdaQueryWrapper<MetaDataAuth> metaDataAuthLambdaQueryWrapper = Wrappers.lambdaQuery();
        metaDataAuthLambdaQueryWrapper.eq(MetaDataAuth::getMetaDataId, metaDataId);
        metaDataAuthLambdaQueryWrapper.eq(MetaDataAuth::getUserId, userDto.getAddress());
        metaDataAuthLambdaQueryWrapper.orderByDesc(MetaDataAuth::getApplyAt);
        metaDataAuthLambdaQueryWrapper.last("limit 1");
        MetaDataAuth metaDataAuth = metaDataAuthManager.getOne(metaDataAuthLambdaQueryWrapper);
        if(metaDataAuth != null){
            //检验授权数据是否有效-校验状态审核中
            if (metaDataAuth.getAuditOption() == MetaDataAuthOptionEnum.PENDING && metaDataAuth.getAuthStatus() == MetaDataAuthStatusEnum.PUBLISHED) {
                log.error("Meta data auth audit pending,can not reapply,metaDataId:{}", metaDataId);
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_PENDING_ERROR.getMsg());
            }
            //检验授权数据是否有效-校验状态审核通过
            if (metaDataAuth.getAuditOption() == MetaDataAuthOptionEnum.PASSED && metaDataAuth.getAuthStatus() == MetaDataAuthStatusEnum.PUBLISHED) {
                log.error("Meta data auth unexpired,can not reapply,metaDataId:{}", metaDataId);
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_UNEXPIRED_ERROR.getMsg());
            }
        }

        // 组装申请报文
        Org org = organizationService.findOrgById(metaData.getIdentityId());

        ApplyMetaDataAuthorityRequestDto applyDto = new ApplyMetaDataAuthorityRequestDto();
        applyDto.setUser(userDto.getAddress());
        applyDto.setUserType(UserTypeEnum.checkUserType(userDto.getAddress()));

        MetaDataAuthorityDto metaDataAuthorityDto = new MetaDataAuthorityDto();
        //元数据所属组织
        NodeIdentityDto owner = new NodeIdentityDto();
        owner.setNodeName(org.getNodeName());
        owner.setNodeId(org.getNodeId());
        owner.setIdentityId(org.getIdentityId());
        metaDataAuthorityDto.setOwner(owner);

        //元数据id
        metaDataAuthorityDto.setMetaDataId(metaData.getMetaDataId());

        //元数据怎么使用
        MetaDataUsageRuleDto metaDataUsageDto = new MetaDataUsageRuleDto();
        metaDataUsageDto.setUseType(authType.getValue());
        if (authType == MetaDataAuthTypeEnum.PERIOD) {
            metaDataUsageDto.setStartAt(startAt.getTime());
            metaDataUsageDto.setEndAt(endAt.getTime());
        }
        metaDataUsageDto.setTimes(times);
        metaDataAuthorityDto.setMetaDataUsageDto(metaDataUsageDto);

        applyDto.setAuth(metaDataAuthorityDto);
        applyDto.setSign(sign);
        ApplyMetaDataAuthorityResponseDto responseDto = null;
        try {
            responseDto = grpcAuthService.applyMetaDataAuthority(applyDto);
            if (responseDto.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE || StrUtil.isBlank(responseDto.getMetaDataAuthId())) {
                log.error("元数据授权申请,net处理失败，失败原因：{}", responseDto);
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, responseDto.getMsg());
            }
        } catch (Exception e) {
            log.error("元数据授权申请,net处理失败，返回参数:{}, 失败原因：{}", JSON.toJSONString(responseDto), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_AUTH_METADATA_RPC_ERROR.getMsg());
        }
        // 保存用户授权申请元数据, rpc接口调用失败时，不保存授权元数据
        MetaDataAuth save = new MetaDataAuth();
        save.setMetaDataAuthId(responseDto.getMetaDataAuthId());
        save.setUserIdentityId(org.getIdentityId());
        save.setUserId(userDto.getAddress());
        save.setUserType(com.moirae.rosettaflow.mapper.enums.UserTypeEnum.find(UserTypeEnum.checkUserType(userDto.getAddress())));
        save.setMetaDataId(metaDataId);
        save.setAuthType(authType);
        if(authType == MetaDataAuthTypeEnum.PERIOD){
            save.setStartAt(startAt);
            save.setEndAt(endAt);
        }
        save.setTimes(times);
        save.setExpired(false);
        save.setUsedTimes(0);
        save.setApplyAt(new Date());
        save.setAuditOption(MetaDataAuthOptionEnum.PENDING);
        save.setAuthStatus(MetaDataAuthStatusEnum.PUBLISHED);
        save.setUpdateAt(new Date());

        if (!metaDataAuthManager.save(save)) {
            log.error("元数据授权申请, 保存等待审核元数据失败，userMetaData：{}", save);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_AUTH_SAVE_ERROR.getMsg());
        }
        log.info("元数据授权申请id为：{}", responseDto.getMetaDataAuthId());
    }

    @Override
    public void revoke(String metadataAuthId, String sign) {
        //检查待撤销userMetaData数据
        MetaDataAuth metaDataAuth = metaDataAuthManager.getById(metadataAuthId);
        if (Objects.isNull(metaDataAuth)) {
            log.error("query userMetaData fail by id:{}",metadataAuthId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_NOT_EXIST.getMsg());
        }

        if (!(metaDataAuth.getAuditOption() == MetaDataAuthOptionEnum.PENDING && metaDataAuth.getAuthStatus() == MetaDataAuthStatusEnum.PUBLISHED)) {
            log.error("user auth metaData status error,can not revoke,metadataAuthId:{}, auditOption:{}, authStatus:{}", metadataAuthId, metaDataAuth.getAuditOption(), metaDataAuth.getAuthStatus());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_AUTH_METADATA_REVOKE_ERROR.getMsg());
        }

        //撤销元数据授权
        UserDto userDto = commonService.getCurrentUser();
        RevokeMetaDataAuthorityRequestDto requestDto = new RevokeMetaDataAuthorityRequestDto();
        requestDto.setUser(userDto.getAddress());
        requestDto.setMetadataAuthId(metadataAuthId);
        requestDto.setSign(sign);
        requestDto.setUserType(UserTypeEnum.checkUserType(userDto.getAddress()));
        RevokeMetadataAuthorityResponseDto responseDto = grpcAuthService.revokeMetadataAuthority(requestDto);
        if (responseDto.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("撤销元数据授权,net处理失败，失败原因：{}", responseDto);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, responseDto.getMsg());
        }
        //更新用户授权元数据，状态为已撤销数据授权
        metaDataAuth.setAuthStatus(MetaDataAuthStatusEnum.REVOKED);
        if (!metaDataAuthManager.updateById(metaDataAuth)) {
            log.error("更新用户授权数据信息的状态auth_metadata_state失败,id：{}", metadataAuthId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_AUTH_METADATA_STATE_UPDATE_FAIL.getMsg());
        }
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
    public void checkMetaDataAuthListEffective(Set<String> metaDataIdList) {
        LambdaQueryWrapper<MetaDataAuth> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaDataAuth::getAuthStatus, MetaDataAuthStatusEnum.PUBLISHED);
        queryWrapper.in(MetaDataAuth::getMetaDataId, metaDataIdList);
        queryWrapper.groupBy(MetaDataAuth::getMetaDataId);
        int count = metaDataAuthManager.count(queryWrapper);
        if(count != metaDataIdList.size()){
            //无效元数据
            log.error("有授权数据已过期，请检查, metaDataIdList:{}", metaDataIdList);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_DATA_EXPIRE.getMsg());
        }
    }
}
