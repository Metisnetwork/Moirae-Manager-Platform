package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.common.utils.BatchExecuteUtil;
import com.moirae.rosettaflow.grpc.client.AuthServiceClient;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.mapper.domain.Organization;
import com.moirae.rosettaflow.service.IDataSyncService;
import com.moirae.rosettaflow.service.IOrganizationService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 同步组织状态定时任务
 *
 * @author hudenian
 * @date 2021/8/23
 */
@Slf4j
@Component
public class SyncOrganizationStatusTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private AuthServiceClient authServiceClient;
    @Resource
    private IOrganizationService organizationService;
    @Resource
    private IDataSyncService dataSyncService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncOrganizationStatusTask")
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
    private void batchUpdateOrg(List<NodeIdentityDto> nodeIdentityDtoList) {
        List<Organization> orgList = nodeIdentityDtoList.stream()
                .map(nodeIdentityDto -> {
                    Organization organization = new Organization();
                    organization.setIdentityId(nodeIdentityDto.getIdentityId());
                    organization.setNodeId(nodeIdentityDto.getNodeId());
                    organization.setNodeName(nodeIdentityDto.getNodeName());
                    organization.setStatus(nodeIdentityDto.getStatus().byteValue());
//                    organization.setUpdateTime(new Date(nodeIdentityDto.getUpdateAt()));
                    return organization;
                })
                .collect(Collectors.toList());
        //更新
        BatchExecuteUtil.batchExecute(sysConfig.getBatchSize(), orgList, list -> {
            organizationService.batchUpdate(list);
        });
    }
}
