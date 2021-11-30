package com.moirae.rosettaflow.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import com.moirae.rosettaflow.mapper.domain.UserMetaData;
import com.moirae.rosettaflow.service.IUserMetaDataService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 同步用户元数据授权列列表（当用户对元数据做授权申请后，redis记录设置已申请，此定时任务判断有待申请记录就启动同步，否则不同步）
 * @author hudenian
 * @date 2021/8/25
 */
@Slf4j
@Component
@Profile({"prod", "test", "local", "xty", "dev"})
public class SyncUserDataAuthTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private GrpcAuthService grpcAuthService;

    @Resource
    private IUserMetaDataService userMetaDataService;

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 2 * 1000)
    @Transactional(rollbackFor = RuntimeException.class)
    @Lock(keys = "SyncUserDataAuthTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            // 获取用户flow平台待审核的授权数据
            List<UserMetaData> userMetaDataOldList = userMetaDataService.getByAuthStatus(UserMetaDataAuditEnum.AUDIT_PENDING.getValue());
            // 获取所有待审核的用户数据id
            List<String> metaDataAuthIdList = new ArrayList<>();
            if (CollUtil.isNotEmpty(userMetaDataOldList)) {
                for (UserMetaData userMetaData : userMetaDataOldList) {
                    metaDataAuthIdList.add(userMetaData.getMetadataAuthId());
                }
            }

            // 查询调度服务，获取用户授权相关数据
            List<GetMetaDataAuthorityDto> metaDataAuthorityDtoList = grpcAuthService.getGlobalMetadataAuthorityList();
            if (CollUtil.isEmpty(metaDataAuthorityDtoList)) {
                return;
            }
            // 处理待审核数据为0时的情况
            if (0 == metaDataAuthIdList.size()) {
                // 不更新数据
                if (metaDataAuthorityDtoList.size() == userMetaDataService.count()) {
                    log.info("用户授权数据同步, 数据未变化不更新, net同步数据量:{}条", metaDataAuthorityDtoList.size());
                    return;
                }
                log.info("moirae管理台与net中用户申请授权元数据信息记录数不一致，开始更新>>>>");
                // 更新数据
                // 清空原来授权数据
                userMetaDataService.truncate();
                this.batchDealUserAuthData(metaDataAuthorityDtoList, SysConstant.INSERT);
                log.info("moirae管理台与net中用户申请授权元数据信息记录数不一致，更新结束>>>>");
                return;
            }
            log.info("待审核用户申请授权元数据信息同步开始>>>>");
            // 处理待审核数据不为0的情况
            List<GetMetaDataAuthorityDto>  updateAuthorityDtoList = new ArrayList<>();
            for (GetMetaDataAuthorityDto authorityDto : metaDataAuthorityDtoList) {
                if (metaDataAuthIdList.contains(authorityDto.getMetaDataAuthId())) {
                    if (AuditMetaDataOptionEnum.AUDIT_PASSED.getValue() == authorityDto.getAuditMetaDataOption()
                    || AuditMetaDataOptionEnum.AUDIT_REFUSED.getValue() == authorityDto.getAuditMetaDataOption()) {
                        updateAuthorityDtoList.add(authorityDto);
                    }
                }
            }
            this.batchDealUserAuthData(updateAuthorityDtoList, SysConstant.UPDATE);
        } catch (Exception e) {
            log.error("从net同步用户元数据授权列表失败, 失败原因:{}, 错误信息:{}", e.getMessage(), e);
        }
        log.info("用户申请授权元数据信息同步结束, 总耗时:{}ms", (DateUtil.current() - begin));
    }

    /**
     * 批量处理用户授权数据
     * @param metaDataList 需更新数据
     * @param saveFlag 处理方式
     */
    private void batchDealUserAuthData(List<GetMetaDataAuthorityDto> metaDataList, String saveFlag){
        if (0 == metaDataList.size()) {
            return;
        }
        List<UserMetaData> userMetaDataList = this.getUserMetaDataList(metaDataList);
        int insertLength = userMetaDataList.size();
        int i = 0;
        log.info("用户申请授权元数据据批量处理开始，总条数:{}", insertLength);
        while (insertLength > sysConfig.getBatchSize()){
            long begin = DateUtil.current();
            if (SysConstant.INSERT.equals(saveFlag)) {
                userMetaDataService.batchInsert(userMetaDataList.subList(i, i + sysConfig.getBatchSize()));
            }
            if (SysConstant.UPDATE.equals(saveFlag)) {
                userMetaDataService.batchUpdate(userMetaDataList.subList(i, i + sysConfig.getBatchSize()));
            }
            log.info("用户申请授权元数据据批量处理，处理方式:{}, 开始条数:{}, 结束条数:{}, 耗时:{}ms", saveFlag, i, i + sysConfig.getBatchSize(), DateUtil.current() - begin);
            i = i + sysConfig.getBatchSize();
            insertLength = insertLength - i;

        }
        if (insertLength > 0) {
            long begin = DateUtil.current();
            if (SysConstant.INSERT.equals(saveFlag)) {
                userMetaDataService.batchInsert(userMetaDataList.subList(i, i + insertLength));
            }
            if (SysConstant.UPDATE.equals(saveFlag)) {
                userMetaDataService.batchUpdate(userMetaDataList.subList(i, i + insertLength));
            }
            log.info("用户申请授权元数据据批量处理，处理方式:{}, 开始条数:{}, 结束条数:{}, 耗时:{}ms", saveFlag, i, i + insertLength, DateUtil.current() - begin);
        }
    }

    /**
     * 处理调度服务数据
     * @param metaDataAuthorityDtoList 需处理数据
     * @return UserMetaData集合
     */
    private List<UserMetaData> getUserMetaDataList(List<GetMetaDataAuthorityDto> metaDataAuthorityDtoList) {
        List<UserMetaData> userMetaDataList = new ArrayList<>();
        for (GetMetaDataAuthorityDto authorityDto : metaDataAuthorityDtoList) {
            UserMetaData userMetaData = new UserMetaData();
            userMetaData.setMetaDataId(authorityDto.getMetaDataAuthorityDto().getMetaDataId());
            userMetaData.setIdentityId(authorityDto.getMetaDataAuthorityDto().getOwner().getIdentityId());
            userMetaData.setIdentityName(authorityDto.getMetaDataAuthorityDto().getOwner().getNodeName());
            userMetaData.setNodeId(authorityDto.getMetaDataAuthorityDto().getOwner().getNodeId());
            try {
                userMetaData.setAddress(AddressChangeUtils.convert0xAddress(authorityDto.getUser()));
            } catch (Exception e) {
                // 如果钱包地址不合法，此条数据过滤不入库
                userMetaData.setAddress(authorityDto.getUser());
                log.error("钱包地址非法, address:{}, 错误信息:{}", authorityDto.getUser(), e);
            }
            userMetaData.setAuthStatus(authorityDto.getAuditMetaDataOption().byteValue());
            userMetaData.setApplyTime(DateUtil.date(authorityDto.getApplyAt()));
            userMetaData.setAuditTime((Objects.isNull(authorityDto.getAuditAt()) || authorityDto.getAuditAt() == 0) ? null : DateUtil.date(authorityDto.getAuditAt()));
            userMetaData.setExpire(authorityDto.getMetadataUsedQuoDto().isExpire() ? MetaDataExpireStatusEnum.expire.getValue() : MetaDataExpireStatusEnum.un_expire.getValue());
            userMetaData.setUsedTimes(authorityDto.getMetadataUsedQuoDto().getUsedTimes());
            userMetaData.setAuthMetadataState(authorityDto.getMetadataAuthorityState().byteValue());
            userMetaData.setAuditSuggestion(authorityDto.getAuditSuggestion());
            userMetaData.setMetadataAuthId(authorityDto.getMetaDataAuthId());

            // 授权方式: 1-按时间, 2-按次数, 3-永久
            userMetaData.setAuthType(authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getUseType().byteValue());
            // 按次数授权
            if (AuthTypeEnum.NUMBER.getValue() == userMetaData.getAuthType()) {
                userMetaData.setAuthValue(authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getTimes());
            }
            // 按时间授权
            if (AuthTypeEnum.TIME.getValue() == userMetaData.getAuthType()) {
                //授权开始时间
                Long authBeginTime = authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getStartAt();
                if (null != authBeginTime && authBeginTime > 0) {
                    userMetaData.setAuthBeginTime(DateUtil.date(authBeginTime));
                }
                //授权结束时间
                Long authEndTime = authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getEndAt();
                if (null != authEndTime && authEndTime > 0) {
                    userMetaData.setAuthEndTime(DateUtil.date(authEndTime));
                }
                // 如果按时间授权，超过授权截止日期，AuthMetadataState及expire由于net没有更新，flow要自己更新
                if (new Date().after(userMetaData.getAuthEndTime())) {
                    userMetaData.setExpire(ExpireTypeEnum.EXPIRE.getValue());
                    userMetaData.setAuthMetadataState(UserMetaDataAuthorithStateEnum.INVALID.getValue());
                    userMetaData.setAuthStatus(AuditMetaDataOptionEnum.AUDIT_REFUSED.getValue());
                }
            }
            userMetaDataList.add(userMetaData);
        }
        return userMetaDataList;
    }
}
