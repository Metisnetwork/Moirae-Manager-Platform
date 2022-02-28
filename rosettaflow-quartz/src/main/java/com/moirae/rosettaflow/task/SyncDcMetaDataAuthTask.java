package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.common.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import com.moirae.rosettaflow.mapper.domain.MetaDataAuth;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthOptionEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthStatusEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthTypeEnum;
import com.moirae.rosettaflow.mapper.enums.UserTypeEnum;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.IDataSyncService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同步用户元数据授权列列表（当用户对元数据做授权申请后，redis记录设置已申请，此定时任务判断有待申请记录就启动同步，否则不同步）
 *
 * @author hudenian
 * @date 2021/8/25
 */
@Slf4j
@Component
public class SyncDcMetaDataAuthTask {

    @Resource
    private GrpcAuthService grpcAuthService;

    @Resource
    private DataService dataService;

    @Resource
    private IDataSyncService dataSyncService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncDcMetaDataAuthTask")
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
        List<MetaDataAuth> metaDataAuthList = new ArrayList<>();
        metaDataList.stream().forEach(item -> {
            MetaDataAuth metaDataAuth = new MetaDataAuth();
            metaDataAuth.setMetaDataAuthId(item.getMetaDataAuthId());
            metaDataAuth.setUserIdentityId(item.getMetaDataAuthorityDto().getOwner().getIdentityId());
            metaDataAuth.setUserId(item.getUser());
            metaDataAuth.setUserType(UserTypeEnum.find(item.getUserType()));
            metaDataAuth.setMetaDataId(item.getMetaDataAuthorityDto().getMetaDataId());
            metaDataAuth.setAuthType(MetaDataAuthTypeEnum.find(item.getMetaDataAuthorityDto().getMetaDataUsageDto().getUseType()));

            //授权开始时间
            Long authBeginTime = item.getMetaDataAuthorityDto().getMetaDataUsageDto().getStartAt();
            if (null != authBeginTime && authBeginTime > 0) {
                metaDataAuth.setStartAt(new Date(authBeginTime));
            }
            //授权结束时间
            Long authEndTime = item.getMetaDataAuthorityDto().getMetaDataUsageDto().getEndAt();
            if (null != authEndTime && authEndTime > 0) {
                metaDataAuth.setEndAt(new Date(authEndTime));
            }
            Integer times = item.getMetaDataAuthorityDto().getMetaDataUsageDto().getTimes();
            if (null != times && times > 0) {
                metaDataAuth.setTimes(times);
            }
            metaDataAuth.setExpired(item.getMetadataUsedQuoDto().isExpire());
            metaDataAuth.setUsedTimes(item.getMetadataUsedQuoDto().getUsedTimes());
            Long applyAt = item.getApplyAt();
            if (null != applyAt && applyAt > 0) {
                metaDataAuth.setApplyAt(new Date(applyAt));
            }
            metaDataAuth.setAuditOption(MetaDataAuthOptionEnum.find(item.getAuditMetaDataOption()));
            metaDataAuth.setAuditDesc(item.getAuditSuggestion());
            Long auditAt = item.getAuditAt();
            if (null != auditAt && auditAt > 0) {
                metaDataAuth.setAuditAt(new Date(auditAt));
            }
            metaDataAuth.setAuthStatus(MetaDataAuthStatusEnum.find(item.getMetadataAuthorityState()));
            metaDataAuth.setUpdateAt(new Date(item.getUpdateAt()));
            metaDataAuthList.add(metaDataAuth);
        });

        dataService.batchReplaceAuth(metaDataAuthList);
    }
}
