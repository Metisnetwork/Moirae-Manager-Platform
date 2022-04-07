package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.grpc.client.PowerServiceClient;
import com.moirae.rosettaflow.grpc.service.GetGlobalPowerDetailResponse;
import com.moirae.rosettaflow.mapper.domain.PowerServer;
import com.moirae.rosettaflow.mapper.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.service.DataSyncService;
import com.moirae.rosettaflow.service.PowerService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SyncDcPowerServerTask {

    @Resource
    private PowerServiceClient powerServiceClient;
    @Resource
    private DataSyncService dataSyncService;
    @Resource
    private PowerService powerService;

    @Scheduled(fixedDelay = 5 * 1000)
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
                                .getPower()
                                .getUpdateAt();
                    });
        } catch (Exception e) {
            log.error("算力信息同步,从net同步算力失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("算力信息同步结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void batchUpdatePower(List<GetGlobalPowerDetailResponse> getGlobalPowerDetailResponseList) {
        List<PowerServer> powerServerList = new ArrayList<>();

        getGlobalPowerDetailResponseList.stream().forEach(item->{
            PowerServer powerServer = new PowerServer();
            powerServer.setId(item.getPowerId());
            powerServer.setIdentityId(item.getOwner().getIdentityId());
            powerServer.setMemory(item.getPower().getInformation().getTotalMem());
            powerServer.setCore(item.getPower().getInformation().getTotalProcessor());
            powerServer.setBandwidth(item.getPower().getInformation().getTotalBandwidth());
            powerServer.setUsedMemory(item.getPower().getInformation().getUsedMem());
            powerServer.setUsedCore(item.getPower().getInformation().getUsedProcessor());
            powerServer.setUsedBandwidth(item.getPower().getInformation().getUsedBandwidth());
            powerServer.setPublishedAt(new Date(item.getPower().getPublishAt()));
            powerServer.setStatus(item.getPower().getState().getNumber());
            powerServer.setUpdateAt(new Date(item.getPower().getUpdateAt()));
            powerServerList.add(powerServer);
        });
        powerService.batchReplace(powerServerList);
    }
}
