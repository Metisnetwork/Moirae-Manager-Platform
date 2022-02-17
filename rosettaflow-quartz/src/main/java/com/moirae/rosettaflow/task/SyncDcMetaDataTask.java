package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.common.utils.BatchExecuteUtil;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataColumnDetailDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcMetaDataService;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataDetails;
import com.moirae.rosettaflow.service.IDataSyncService;
import com.moirae.rosettaflow.service.IMetaDataDetailsService;
import com.moirae.rosettaflow.service.IMetaDataService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 同步元数据定时任务, 多久同步一次待确认
 *
 * @author hudenian
 * @date 2021/8/23
 */
@Slf4j
@Component
public class SyncDcMetaDataTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private GrpcMetaDataService grpcMetaDataService;

    @Resource
    private IMetaDataService metaDataService;

    @Resource
    private IMetaDataDetailsService metaDataDetailsService;

    @Resource
    private IDataSyncService dataSyncService;

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
                        // 批量更新元数据详情
                        for (MetaDataDetailResponseDto metaDataDetailResponseDto : grpcResponseList) {
                            String metaDataId = metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getMetaDataId();
                            List<MetaDataColumnDetailDto> columnList = metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataColumnDetailDtoList();
                            //  批量更新元数据详情
                            this.batchUpdateMetaDataDetails(metaDataId, columnList);
                        }
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
        List<MetaData> newMetaDataList = this.getMetaDataList(metaDataDetailResponseDtoList);

        List<String> metaDataIdList = newMetaDataList.stream()
                .map(MetaData::getMetaDataId)
                .collect(Collectors.toList());

        //查询已存在的数据
        List<String> existMetaDataIdList = metaDataService.existMetaDataIdList(metaDataIdList);

        //需要更新的数据
        List<MetaData> needUpdate = newMetaDataList.stream()
                .filter(metaData -> existMetaDataIdList.contains(metaData.getMetaDataId()))
                .collect(Collectors.toList());
        //需要新增的数据
        List<MetaData> needInsert = newMetaDataList.stream()
                .filter(metaData -> !existMetaDataIdList.contains(metaData.getMetaDataId()))
                .collect(Collectors.toList());

        //批量更新
        BatchExecuteUtil.batchExecute(sysConfig.getBatchSize(), needUpdate, list -> {
            metaDataService.batchUpdate(list);
        });
        //批量新增
        BatchExecuteUtil.batchExecute(sysConfig.getBatchSize(), needInsert, list -> {
            metaDataService.batchInsert(list);
        });
    }

    /**
     * 批量插入元数据详情
     *
     * @param metaDataId 元数据id
     * @param columnList 需更新数据
     */
    private void batchUpdateMetaDataDetails(String metaDataId, List<MetaDataColumnDetailDto> columnList) {
        if (0 == columnList.size()) {
            return;
        }
        List<MetaDataDetails> newMetaDataDetailsList = this.getMetaDataDetailsList(metaDataId, columnList);

        //查询已存在的数据
        List<MetaDataDetails> existMetaDataDetails = metaDataDetailsService.existMetaDataIdAndColumnList(newMetaDataDetailsList);

        //需要更新的数据
        List<MetaDataDetails> needUpdate = newMetaDataDetailsList.stream()
                .filter(metaDataDetails -> existMetaDataDetails.contains(metaDataDetails))
                .collect(Collectors.toList());
        //需要新增的数据
        List<MetaDataDetails> needInsert = newMetaDataDetailsList.stream()
                .filter(metaDataDetails -> !existMetaDataDetails.contains(metaDataDetails))
                .collect(Collectors.toList());

        //更新
        BatchExecuteUtil.batchExecute(sysConfig.getBatchSize(), needUpdate, list -> {
            metaDataDetailsService.batchUpdateByMetaDataIdAndColumnIndex(list);
        });

        //新增
        BatchExecuteUtil.batchExecute(sysConfig.getBatchSize(), needInsert, list -> {
            metaDataDetailsService.batchInsert(list);
        });
    }

    private List<MetaDataDetails> getMetaDataDetailsList(String metaDataId, List<MetaDataColumnDetailDto> columnList) {
        List<MetaDataDetails> metaDataDetailsList = new ArrayList<>();
        for (MetaDataColumnDetailDto metaDataColumnDetailDto : columnList) {
            MetaDataDetails metaDataDetail = new MetaDataDetails();
            metaDataDetail.setMetaDataId(metaDataId);
            metaDataDetail.setColumnIndex(metaDataColumnDetailDto.getIndex());
            metaDataDetail.setColumnName(metaDataColumnDetailDto.getName());
            metaDataDetail.setColumnType(metaDataColumnDetailDto.getType());
            metaDataDetail.setColumnSize((long) metaDataColumnDetailDto.getSize());
            metaDataDetail.setColumnDesc(metaDataColumnDetailDto.getComment());
            metaDataDetailsList.add(metaDataDetail);
        }
        return metaDataDetailsList;
    }

    private List<MetaData> getMetaDataList(List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList) {
        List<MetaData> metaDataList = new ArrayList<>();
        for (MetaDataDetailResponseDto metaDataDetailResponseDto : metaDataDetailResponseDtoList) {
            MetaData metaData = new MetaData();
            metaData.setIdentityId(metaDataDetailResponseDto.getOwner().getIdentityId());
            metaData.setIdentityName(metaDataDetailResponseDto.getOwner().getNodeName());
            metaData.setNodeId(metaDataDetailResponseDto.getOwner().getNodeId());
            //元数据id为空不入库
            if (StrUtil.isBlank(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getMetaDataId())) {
                // 跳过元数据为null的数据
                log.error("MetaDataId is null jump over, metaDataSummary:{}", metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary());
            }
            metaData.setMetaDataId(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getMetaDataId());
            metaData.setFileId(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getOriginId());
            metaData.setDataName(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getTableName());
            metaData.setDataDesc(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getDesc());
            metaData.setFilePath(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getFilePath());
            metaData.setRows(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getRows());
            metaData.setColumns(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getColumns());
            metaData.setSize(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getSize());
            metaData.setFileType(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getFileType().byteValue());
            metaData.setHasTitle(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getHasTitle() ? (byte) 1 : (byte) 0);
            metaData.setIndustry(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getIndustry());
            metaData.setDataStatus(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getDataState().byteValue());
            Long publishAt = metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getPublishAt();
            Long updateAt = metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getUpdateAt();
            metaData.setPublishAt(publishAt > 0 ? new Date(publishAt) : null);
            metaData.setUpdateAt(updateAt > 0 ? new Date(updateAt) : null);
            metaDataList.add(metaData);
        }
        return metaDataList;
    }

}
