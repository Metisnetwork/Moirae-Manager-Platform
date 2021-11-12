package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 同步用户元数据授权列列表（当用户对元数据做授权申请后，redis记录设置已申请，此定时任务判断有待申请记录就启动同步，否则不同步）
 */
@Slf4j
@Component
@Profile({"prod", "test", "local", "xty"})
public class SyncUserDataAuthTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private GrpcAuthService grpcAuthService;

    @Resource
    private IUserMetaDataService userMetaDataService;

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 2 * 1000)
    @Transactional(rollbackFor = RuntimeException.class)
    @Lock(keys = "SyncUserDataAuthTask")
    public void run() {
        log.info("用户申请授权元数据信息同步开始>>>>");
        long begin;
        List<GetMetaDataAuthorityDto> metaDataAuthorityDtoList;
        try {
            metaDataAuthorityDtoList = grpcAuthService.getGlobalMetadataAuthorityList();
            if (null == metaDataAuthorityDtoList || metaDataAuthorityDtoList.size() < 1) {
                return;
            } else {
                //删除原来授权数据
                userMetaDataService.truncate();
            }
        } catch (Exception e) {
            log.error("从net同步用户元数据授权列表失败,失败原因：{}", e.getMessage(), e);
            return;
        }

        List<UserMetaData> userMetaDataList = new ArrayList<>();
        UserMetaData userMetaData;
        int userMetaDataSize = 0;

        for (GetMetaDataAuthorityDto authorityDto : metaDataAuthorityDtoList) {
            userMetaData = getUserMetaData(authorityDto);
            //如果钱包地址不合法，此条数据过滤不入库
            if (null == userMetaData) {
                continue;
            }

            userMetaDataList.add(userMetaData);
            ++userMetaDataSize;
            if (userMetaDataSize % sysConfig.getBatchSize() == 0) {
                begin = DateUtil.currentSeconds();
                log.info("用户申请授权元数据据更新{}条数据开始", userMetaDataSize);
                userMetaDataService.batchInsert(userMetaDataList);
                log.info("用户申请授权元数据据更新{}条数据结束一共用时{}秒", userMetaDataSize, DateUtil.currentSeconds() - begin);
                userMetaDataList.clear();
            }
        }
        if (userMetaDataList.size() > 0) {
            begin = DateUtil.currentSeconds();
            log.info("用户申请授权元数据据更新{}条数据开始", userMetaDataSize);
            userMetaDataService.batchInsert(userMetaDataList);
            log.info("用户申请授权元数据据更新{}条数据结束一共用时{}秒", userMetaDataSize, DateUtil.currentSeconds() - begin);
        }
        log.info("用户申请授权元数据信息同步结束>>>>");
    }

    private UserMetaData getUserMetaData(GetMetaDataAuthorityDto authorityDto) {
        UserMetaData userMetaData;
        userMetaData = new UserMetaData();
        userMetaData.setMetaDataId(authorityDto.getMetaDataAuthorityDto().getMetaDataId());
        userMetaData.setIdentityId(authorityDto.getMetaDataAuthorityDto().getOwner().getIdentityId());
        userMetaData.setIdentityName(authorityDto.getMetaDataAuthorityDto().getOwner().getNodeName());
        userMetaData.setNodeId(authorityDto.getMetaDataAuthorityDto().getOwner().getNodeId());

        String address;
        try {
            address = AddressChangeUtils.convert0xAddress(authorityDto.getUser());
            userMetaData.setAddress(address);
        } catch (Exception e) {
            log.error("钱包地址{}非法", authorityDto.getUser(), e);
            userMetaData.setAddress(authorityDto.getUser());
            return null;
        }

        userMetaData.setAuthType(authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getUseType().byteValue());
        //授权次数
        Integer times = authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getTimes();
        if (null != times && times > 0) {
            userMetaData.setAuthValue(times);
        }
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

        int auditStatus = authorityDto.getAuditMetaDataOption();
        userMetaData.setAuthStatus((byte) auditStatus);
        userMetaData.setApplyTime(DateUtil.date(authorityDto.getApplyAt()));
        userMetaData.setAuditTime((Objects.isNull(authorityDto.getAuditAt()) || authorityDto.getAuditAt() == 0) ? null : DateUtil.date(authorityDto.getAuditAt()));
        userMetaData.setExpire(authorityDto.getMetadataUsedQuoDto().isExpire() ? MetaDataExpireStatusEnum.expire.getValue() : MetaDataExpireStatusEnum.un_expire.getValue());
        userMetaData.setUsedTimes(authorityDto.getMetadataUsedQuoDto().getUsedTimes());
        userMetaData.setAuthMetadataState(authorityDto.getMetadataAuthorityState().byteValue());
        userMetaData.setAuditSuggestion(authorityDto.getAuditSuggestion());
        //如果按时间授权，超过授权截止日期，AuthMetadataState及expire由于net没有更新，flow要自己更新
        if (userMetaData.getAuthType() == AuthTypeEnum.TIME.getValue() && new Date().after(userMetaData.getAuthEndTime())) {
            userMetaData.setExpire(ExpireTypeEnum.EXPIRE.getValue());
            userMetaData.setAuthMetadataState(UserMetaDataAuthorithStateEnum.INVALID.getValue());
            userMetaData.setAuthStatus(AuditMetaDataOptionEnum.AUDIT_REFUSED.getValue());
        }
        return userMetaData;
    }
}
