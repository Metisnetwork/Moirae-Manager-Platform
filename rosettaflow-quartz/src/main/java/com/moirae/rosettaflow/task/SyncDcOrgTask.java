package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.grpc.client.impl.GrpcAuthServiceClientImpl;
import com.moirae.rosettaflow.grpc.service.types.Organization;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.mapper.enums.OrgStatusEnum;
import com.moirae.rosettaflow.service.DataSyncService;
import com.moirae.rosettaflow.service.OrgService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据中心的组织同步
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncDcOrgTask {

    @Resource
    private GrpcAuthServiceClientImpl authServiceClient;
    @Resource
    private OrgService organizationService;
    @Resource
    private DataSyncService dataSyncService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncDcOrgTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            dataSyncService.sync(DataSyncTypeEnum.ORG_STATUS.getDataType(),DataSyncTypeEnum.ORG_STATUS.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                    (latestSynced) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                        return authServiceClient.getIdentityList(latestSynced);
                    },
                    (grpcResponseList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                        // 批量更新
                        this.batchUpdateOrg(grpcResponseList);
                    },
                    (grpcResponseList) -> {//4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
                        return grpcResponseList
                                .get(grpcResponseList.size() - 1)
                                .getUpdateAt();
                    });
        } catch (Exception e) {
            log.error("组织信息同步,从net同步元数据失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("组织信息同步结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    /**
     * @param nodeIdentityDtoList 需更新数据
     */
    private void batchUpdateOrg(List<Organization> nodeIdentityDtoList) {
        List<Org> orgList = nodeIdentityDtoList.stream().map(nodeIdentityDto -> {
                    Org org = new Org();
                    org.setIdentityId(nodeIdentityDto.getIdentityId());
                    org.setNodeId(nodeIdentityDto.getNodeId());
                    org.setNodeName(nodeIdentityDto.getNodeName());
                    org.setImageUrl(nodeIdentityDto.getImageUrl());
                    org.setDetails(nodeIdentityDto.getDetails());
                    org.setStatus(OrgStatusEnum.find(nodeIdentityDto.getStatus().getNumber()));
                    org.setUpdateAt(new Date(nodeIdentityDto.getUpdateAt()));
                    return org;
                })
                .collect(Collectors.toList());
        //更新
        organizationService.batchReplace(orgList);
    }
}
