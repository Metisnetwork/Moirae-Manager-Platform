package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataSummaryDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcMetaDataService;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import com.moirae.rosettaflow.mapper.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataFileTypeEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataStatusEnum;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.DataSyncService;
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
public class SyncDcMetaDataTask {

    @Resource
    private GrpcMetaDataService grpcMetaDataService;

    @Resource
    private DataService metaDataService;

    @Resource
    private DataSyncService dataSyncService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncDcMetaDataTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            dataSyncService.sync(DataSyncTypeEnum.META_DATA.getDataType(),DataSyncTypeEnum.META_DATA.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                    (latestSynced) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                        return grpcMetaDataService.getGlobalMetadataDetailList(latestSynced);
                    },
                    (grpcResponseList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                        // 批量更新元数据
                        this.batchUpdateMetaData(grpcResponseList);
                    },
                    (grpcResponseList) -> {//4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
                        return grpcResponseList
                                .get(grpcResponseList.size() - 1)
                                .getMetaDataDetailDto()
                                .getMetaDataSummary()
                                .getUpdateAt();
                    });
        } catch (Exception e) {
            log.error("元数据信息同步,从net同步元数据失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("元数据信息同步结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    /**
     * @param metaDataDetailResponseDtoList 需更新数据
     */
    private void batchUpdateMetaData(List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList) {
        List<MetaData> metaDataList = new ArrayList<>();
        List<MetaDataColumn> metaDataColumnList = new ArrayList<>();
        metaDataDetailResponseDtoList.stream().forEach(item -> {
            MetaDataSummaryDto dataSummaryDto = item.getMetaDataDetailDto().getMetaDataSummary();
            MetaData metaData = new MetaData();
            metaData.setMetaDataId(dataSummaryDto.getMetaDataId());
            metaData.setOriginId(dataSummaryDto.getOriginId());
            metaData.setIdentityId(item.getOwner().getIdentityId());
            metaData.setFileName(dataSummaryDto.getTableName());
            metaData.setFilePath(dataSummaryDto.getFilePath());
            metaData.setFileType(MetaDataFileTypeEnum.find(dataSummaryDto.getFileType()));
            metaData.setIndustry(dataSummaryDto.getIndustry());
            metaData.setSize(dataSummaryDto.getSize());
            metaData.setRows(dataSummaryDto.getRows());
            metaData.setColumns(dataSummaryDto.getColumns());
            metaData.setPublishedAt(new Date(dataSummaryDto.getPublishAt()));
            metaData.setHasTitle(dataSummaryDto.getHasTitle());
            metaData.setRemarks(dataSummaryDto.getDesc());
            metaData.setStatus(MetaDataStatusEnum.find(dataSummaryDto.getDataState()));
            metaData.setUpdateAt(new Date(dataSummaryDto.getUpdateAt()));
            metaDataList.add(metaData);

            item.getMetaDataDetailDto().getMetaDataColumnDetailDtoList().forEach(subItem ->{
                MetaDataColumn metaDataColumn = new MetaDataColumn();
                metaDataColumn.setMetaDataId(metaData.getMetaDataId());
                metaDataColumn.setColumnIdx(subItem.getIndex());
                metaDataColumn.setColumnName(subItem.getName());
                metaDataColumn.setColumnType(subItem.getType());
                metaDataColumn.setColumnSize(subItem.getSize());
                metaDataColumn.setRemarks(subItem.getComment());
                metaDataColumnList.add(metaDataColumn);
            });
        });

        metaDataService.batchReplace(metaDataList, metaDataColumnList);
    }
}
