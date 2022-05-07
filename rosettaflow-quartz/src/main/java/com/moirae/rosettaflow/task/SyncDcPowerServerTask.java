package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.grpc.client.impl.GrpcPowerServiceClientImpl;
import com.moirae.rosettaflow.grpc.service.GetGlobalPowerDetail;
import com.moirae.rosettaflow.grpc.service.types.Organization;
import com.moirae.rosettaflow.grpc.service.types.PowerUsageDetail;
import com.moirae.rosettaflow.mapper.domain.PowerServer;
import com.moirae.rosettaflow.mapper.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.service.DataSyncService;
import com.moirae.rosettaflow.service.PowerService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncDcPowerServerTask {

    @Resource
    private GrpcPowerServiceClientImpl powerServiceClient;
    @Resource
    private DataSyncService dataSyncService;
    @Resource
    private PowerService powerService;

//    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncDcPowerServer")
    public void run() {
        long begin = DateUtil.current();
        try {
            dataSyncService.sync(DataSyncTypeEnum.POWER.getDataType(),DataSyncTypeEnum.POWER.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                    (latestSynced) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                        return powerServiceClient.getGlobalPowerDetailList(latestSynced);
                    },
                    (grpcResponseList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                        // 批量更新元数据
                        this.batchUpdatePower(grpcResponseList);
                    },
                    (grpcResponseList) -> {//4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
                        return grpcResponseList
                                .get(grpcResponseList.size() - 1)
                                .getInformation()
                                .getUpdateAt();
                    });
        } catch (Exception e) {
            log.error("算力信息同步,从net同步算力失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("算力信息同步结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void batchUpdatePower(List<GetGlobalPowerDetail> getGlobalPowerDetailResponseList) {
        List<PowerServer> powerServerList = new ArrayList<>();

        getGlobalPowerDetailResponseList.stream().forEach(item->{
            Organization owner = item.getOwner();
            PowerUsageDetail information = item.getInformation();
            PowerServer powerServer = new PowerServer();
            powerServer.setId(item.getPowerId());
            powerServer.setIdentityId(owner.getIdentityId());
            powerServer.setMemory(information.getInformation().getTotalMem());
            powerServer.setCore(information.getInformation().getTotalProcessor());
            powerServer.setBandwidth(information.getInformation().getTotalBandwidth());
            powerServer.setUsedMemory(information.getInformation().getUsedMem());
            powerServer.setUsedCore(information.getInformation().getUsedProcessor());
            powerServer.setUsedBandwidth(information.getInformation().getUsedBandwidth());
            powerServer.setPublishedAt(new Date(information.getPublishAt()));
            powerServer.setStatus(information.getStateValue());
            powerServer.setUpdateAt(new Date(information.getUpdateAt()));
            powerServerList.add(powerServer);
        });
        powerService.batchReplace(powerServerList);
    }
}
