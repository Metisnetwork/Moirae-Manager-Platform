package com.platon.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.enums.MetaDataExpireStatusEnum;
import com.platon.rosettaflow.common.utils.AddressChangeUtils;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.service.GrpcAuthService;
import com.platon.rosettaflow.mapper.domain.UserMetaData;
import com.platon.rosettaflow.service.IUserMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 同步用户元数据授权列列表
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

    @Scheduled(fixedDelay = 7200 * 1000, initialDelay = 2 * 1000)
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        if (!sysConfig.isMasterNode()) {
            return;
        }

        log.info("用户申请授权元数据信息同步开始>>>>");
        long begin;
        List<GetMetaDataAuthorityDto> metaDataAuthorityDtoList = grpcAuthService.getMetaDataAuthorityList();

        //授权数据同步成功，删除旧数据
        if (metaDataAuthorityDtoList.size() > 0) {
            //删除原来授权数据
            userMetaDataService.truncate();
        } else {
            return;
        }

        List<UserMetaData> userMetaDataList = new ArrayList<>();
        UserMetaData userMetaData;
        int userMetaDataSize = 0;

        for (GetMetaDataAuthorityDto authorityDto : metaDataAuthorityDtoList) {
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
            userMetaData.setAuditTime(DateUtil.date(authorityDto.getAuditAt()));
            userMetaData.setExpire(authorityDto.getMetadataUsedQuoDto().isExpire() ? MetaDataExpireStatusEnum.expire.getValue() : MetaDataExpireStatusEnum.un_expire.getValue());
            userMetaData.setUsedTimes((long) authorityDto.getMetadataUsedQuoDto().getUsedTimes());

            userMetaDataList.add(userMetaData);
            ++userMetaDataSize;
            if (userMetaDataSize % sysConfig.getBatchSize() == 0) {
                begin = DateUtil.currentSeconds();
                log.info("用户申请授权元数据据更新{}条数据开始", userMetaDataSize);
                userMetaDataService.saveBatch(userMetaDataList);
                log.info("用户申请授权元数据据更新{}条数据结束一共用时{}秒", userMetaDataSize, DateUtil.currentSeconds() - begin);
                userMetaDataList.clear();
            }
        }
        if (userMetaDataList.size() > 0) {
            begin = DateUtil.currentSeconds();
            log.info("用户申请授权元数据据更新{}条数据开始", userMetaDataSize);
            userMetaDataService.saveBatch(userMetaDataList);
            log.info("用户申请授权元数据据更新{}条数据结束一共用时{}秒", userMetaDataSize, DateUtil.currentSeconds() - begin);
        }
        log.info("用户申请授权元数据信息同步结束>>>>");
    }
}
