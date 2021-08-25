package com.platon.rosettaflow.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.service.GrpcAuthService;
import com.platon.rosettaflow.mapper.domain.UserMetaData;
import com.platon.rosettaflow.service.IUserMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    /**
     * 多大数据更新一次数据库
     */
    static final int BATCH_SIZE = 5;

    @Resource
    private SysConfig sysConfig;

    @Resource
    private GrpcAuthService grpcAuthService;

    @Resource
    private IUserMetaDataService userMetaDataService;

    @Scheduled(fixedDelay = 3600000, initialDelay = 1000)
    public void run() {
        if (!sysConfig.isMasterNode()) {
            return;
        }

        log.info("用户申请授权元数据信息同步开始>>>>");
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
            userMetaData.setIdentityName(authorityDto.getMetaDataAuthorityDto().getOwner().getName());
            userMetaData.setNodeId(authorityDto.getMetaDataAuthorityDto().getOwner().getNodeId());

            userMetaData.setAddress(authorityDto.getUser());
            userMetaData.setAuthType(authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getUserType().byteValue());
            //授权次数
            Long times = authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getTimes();
            if (null != times && times > 0) {
                userMetaData.setAuthValue(times);
            }
            //授权开始时间
            Long authBeginTime = authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getStartAt();
            if (null != authBeginTime && authBeginTime > 0) {
                //TODO 毫秒 需要底层联调确认
                userMetaData.setAuthBeginTime(DateUtil.date(authBeginTime));
            }

            //授权结束时间
            Long authEndTime = authorityDto.getMetaDataAuthorityDto().getMetaDataUsageDto().getEndAt();
            if (null != authEndTime && authEndTime > 0) {
                //TODO 毫秒 需要底层联调确认
                userMetaData.setAuthEndTime(DateUtil.date(authEndTime));
            }

            int auditStatus = authorityDto.getAuditMetaDataOption();
            userMetaData.setAuthStatus((byte) auditStatus);
            userMetaData.setApplyTime(DateUtil.date(authorityDto.getApplyAt()));
            userMetaData.setAuditTime(DateUtil.date(authorityDto.getAuditAt()));
            userMetaData.setStatus(StatusEnum.VALID.getValue());

            userMetaDataList.add(userMetaData);
            ++userMetaDataSize;
            if (userMetaDataSize % BATCH_SIZE == 0) {
                userMetaDataService.saveBatch(userMetaDataList);
                userMetaDataList.clear();
            }
        }
        if (userMetaDataList.size() > 0) {
            userMetaDataService.saveBatch(userMetaDataList);
        }
        log.info("用户申请授权元数据信息同步结束>>>>");
    }
}
