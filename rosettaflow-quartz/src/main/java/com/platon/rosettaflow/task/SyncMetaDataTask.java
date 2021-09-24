package com.platon.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataColumnDetailDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.service.GrpcMetaDataService;
import com.platon.rosettaflow.mapper.domain.MetaData;
import com.platon.rosettaflow.mapper.domain.MetaDataDetails;
import com.platon.rosettaflow.service.IMetaDataDetailsService;
import com.platon.rosettaflow.service.IMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 同步元数据定时任务, 多久同步一次待确认
 */
@Slf4j
@Component
public class SyncMetaDataTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private GrpcMetaDataService grpcMetaDataService;

    @Resource
    private IMetaDataService metaDataService;

    @Resource
    private IMetaDataDetailsService metaDataDetailsService;

    @Scheduled(fixedDelay = 3600 * 1000, initialDelay = 10 * 1000)
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        if (!sysConfig.isMasterNode()) {
            return;
        }

        log.info("元数据信息同步开始>>>>");
        long begin;
        List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList = grpcMetaDataService.getGlobalMetadataDetailList();

        //元数据同步成功，删除旧数据
        if (metaDataDetailResponseDtoList.size() > 0) {
            //删除元数据
            metaDataService.truncate();
            //删除元数据详情
            metaDataDetailsService.truncate();
        } else {
            return;
        }

        List<MetaData> newMetaDataList = new ArrayList<>();
        List<MetaDataDetails> newMetaDataDetailsList = new ArrayList<>();
        MetaData metaData;
        MetaDataDetails metaDataDetail;
        int metaDataSize = 0;
        int metaDataDetailSize = 0;

        for (MetaDataDetailResponseDto metaDataDetailResponseDto : metaDataDetailResponseDtoList) {
            metaData = new MetaData();
            metaData.setIdentityId(metaDataDetailResponseDto.getOwner().getIdentityId());
            metaData.setIdentityName(metaDataDetailResponseDto.getOwner().getNodeName());
            metaData.setNodeId(metaDataDetailResponseDto.getOwner().getNodeId());
            //元数据id为空不入库
            if(StrUtil.isBlank(metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getMetaDataId())){
                continue;
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

            //添加元数据简介
            newMetaDataList.add(metaData);
            ++metaDataSize;
            if (metaDataSize % sysConfig.getBatchSize() == 0) {
                begin = DateUtil.currentSeconds();
                log.info("元数据更新{}条数据开始", sysConfig.getBatchSize());
                metaDataService.batchInsert(newMetaDataList);
                log.info("元数据更新{}条数据结束一共用时{}秒", sysConfig.getBatchSize(), DateUtil.currentSeconds() - begin);
                newMetaDataList.clear();
            }

            List<MetaDataColumnDetailDto> columnList = metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataColumnDetailDtoList();
            for (MetaDataColumnDetailDto metaDataColumnDetailDto : columnList) {
                metaDataDetail = new MetaDataDetails();
                metaDataDetail.setMetaDataId(metaData.getMetaDataId());
                metaDataDetail.setColumnIndex(metaDataColumnDetailDto.getIndex());
                metaDataDetail.setColumnName(metaDataColumnDetailDto.getName());
                metaDataDetail.setColumnType(metaDataColumnDetailDto.getType());
                metaDataDetail.setColumnSize((long) metaDataColumnDetailDto.getSize());
                metaDataDetail.setColumnDesc(metaDataColumnDetailDto.getComment());

                //添加元数据详情
                newMetaDataDetailsList.add(metaDataDetail);
                ++metaDataDetailSize;
                if (metaDataDetailSize % sysConfig.getBatchSize() == 0) {
                    begin = DateUtil.currentSeconds();
                    log.info("元数据详情更新{}条数据开始", sysConfig.getBatchSize());
                    metaDataDetailsService.batchInsert(newMetaDataDetailsList);
                    log.info("元数据详情更新{}条数据结束一共用时{}秒", sysConfig.getBatchSize(), DateUtil.currentSeconds() - begin);
                    newMetaDataDetailsList.clear();
                }
            }
        }

        if (newMetaDataList.size() > 0) {
            begin = DateUtil.currentSeconds();
            log.info("元数据更新{}条数据开始", newMetaDataList.size());
            metaDataService.batchInsert(newMetaDataList);
            log.info("元数据更新{}条数据结束一共用时{}秒", newMetaDataList.size(), DateUtil.currentSeconds() - begin);
        }
        if (newMetaDataDetailsList.size() > 0) {
            begin = DateUtil.currentSeconds();
            log.info("元数据详情更新{}条数据开始", newMetaDataDetailsList.size());
            metaDataDetailsService.batchInsert(newMetaDataDetailsList);
            log.info("元数据详情更新{}条数据结束一共用时{}秒", newMetaDataDetailsList.size(), DateUtil.currentSeconds() - begin);
        }
        log.info("元数据信息同步结束>>>>");
    }
}
