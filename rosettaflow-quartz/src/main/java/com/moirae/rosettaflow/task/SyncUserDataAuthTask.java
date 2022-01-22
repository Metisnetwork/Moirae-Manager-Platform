package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
import com.moirae.rosettaflow.common.utils.BatchExecuteUtil;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import com.moirae.rosettaflow.mapper.domain.UserMetaData;
import com.moirae.rosettaflow.service.IDataSyncService;
import com.moirae.rosettaflow.service.IUserMetaDataService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 同步用户元数据授权列列表（当用户对元数据做授权申请后，redis记录设置已申请，此定时任务判断有待申请记录就启动同步，否则不同步）
 *
 * @author hudenian
 * @date 2021/8/25
 */
@Slf4j
@Component
public class SyncUserDataAuthTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private GrpcAuthService grpcAuthService;

    @Resource
    private IUserMetaDataService userMetaDataService;

    @Resource
    private IDataSyncService dataSyncService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncUserDataAuthTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            dataSyncService.sync(DataSyncTypeEnum.DATA_AUTH.getDataType(), DataSyncTypeEnum.DATA_AUTH.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                    (latestSynced) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                        return grpcAuthService.getGlobalMetadataAuthorityList(latestSynced);
                    },
                    (grpcResponseList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                        // 批量更新
                        this.batchDealUserAuthData(grpcResponseList);
                        log.info("用户授权数据同步,同步数据量:{}条", grpcResponseList.size());
                    },
                    (grpcResponseList) -> {//4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
                        return grpcResponseList
                                .get(grpcResponseList.size() - 1)
                                .getUpdateAt();
                    });
        } catch (Exception e) {
            log.error("从net同步用户元数据授权列表失败, 失败原因:{}, 错误信息:{}", e.getMessage(), e);
        }
        log.info("用户申请授权元数据信息同步结束, 总耗时:{}ms", (DateUtil.current() - begin));
    }

    /**
     * 批量处理用户授权数据
     *
     * @param metaDataList 需更新数据
     */
    private void batchDealUserAuthData(List<GetMetaDataAuthorityDto> metaDataList) {
        Date begin = new Date();
        if (0 == metaDataList.size()) {
            return;
        }
        List<UserMetaData> userMetaDataList = this.getUserMetaDataList(metaDataList);
        //查询已存在的数据
        List<String> userMetaDataAuthIdList = userMetaDataList.stream().map(UserMetaData::getMetadataAuthId).collect(Collectors.toList());
        List<String> existMetaDataAuthIdList = userMetaDataService.existMetaDataAuthIdList(userMetaDataAuthIdList);

        //需要更新的数据
        List<UserMetaData> needUpdate = userMetaDataList.stream()
                .filter(metaData -> existMetaDataAuthIdList.contains(metaData.getMetadataAuthId()))
                .collect(Collectors.toList());
        //需要新增的数据
        List<UserMetaData> needInsert = userMetaDataList.stream()
                .filter(metaData -> !existMetaDataAuthIdList.contains(metaData.getMetadataAuthId()))
                .collect(Collectors.toList());

        //批量更新
        BatchExecuteUtil.batchExecute(sysConfig.getBatchSize(), needUpdate, list -> {
            userMetaDataService.batchUpdate(list);
        });
        //批量新增
        BatchExecuteUtil.batchExecute(sysConfig.getBatchSize(), needInsert, list -> {
            userMetaDataService.batchInsert(list);
        });
        log.info("用户申请授权元数据据批量处理，耗时:{}ms", DateUtil.current() - begin.getTime());
    }

    /**
     * 处理调度服务数据
     *
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
