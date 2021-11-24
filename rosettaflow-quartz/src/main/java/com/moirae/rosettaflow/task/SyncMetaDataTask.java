package com.moirae.rosettaflow.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataColumnDetailDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcMetaDataService;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataDetails;
import com.moirae.rosettaflow.service.IMetaDataDetailsService;
import com.moirae.rosettaflow.service.IMetaDataService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 同步元数据定时任务, 多久同步一次待确认
 *
 * @author hudenian
 * @date 2021/8/23
 */
@Slf4j
@Component
@Profile({"prod", "test", "local", "xty"})
public class SyncMetaDataTask {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private GrpcMetaDataService grpcMetaDataService;

    @Resource
    private IMetaDataService metaDataService;

    @Resource
    private IMetaDataDetailsService metaDataDetailsService;

    @Scheduled(fixedDelay = 600 * 1000, initialDelay = 10 * 1000)
    @Transactional(rollbackFor = Exception.class)
    @Lock(keys = "SyncMetaDataTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList = grpcMetaDataService.getGlobalMetadataDetailList();
            if (CollUtil.isEmpty(metaDataDetailResponseDtoList)) {
                return;
            }
            // 从net同步元数据[未发现变更元数据], 故不进行后续同步,
            if (metaDataDetailResponseDtoList.size() == metaDataService.count()) {
                log.info("元数据信息同步, net元数据与flow中元数据记录数一致, net同步数据量:{}条", metaDataDetailResponseDtoList.size());
                return;
            }
            // net更新数据量和flow不一致，重新同步元数据
            // 清空元数据和详情
            this.delOldData();
            // 批量插入元数据
            this.batchInsertMetaData(metaDataDetailResponseDtoList);

            // 批量插入元数据详情
            for (MetaDataDetailResponseDto metaDataDetailResponseDto : metaDataDetailResponseDtoList) {
                String metaDataId = metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataSummary().getMetaDataId();
                List<MetaDataColumnDetailDto> columnList = metaDataDetailResponseDto.getMetaDataDetailDto().getMetaDataColumnDetailDtoList();
                //  批量插入元数据详情
                this.batchInsertMetaDataDetails(metaDataId, columnList);
            }
        } catch (Exception e) {
            log.error("元数据信息同步,从net同步元数据失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("元数据信息同步结束，总耗时:{}ms", DateUtil.current() - begin);
    }


    /**
     * 批量插入元数据
     *
     * @param metaDataDetailResponseDtoList 需更新数据
     */
    private void batchInsertMetaData(List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList) {
        if (0 == metaDataDetailResponseDtoList.size()) {
            return;
        }
        List<MetaData> newMetaDataList = this.getMetaDataList(metaDataDetailResponseDtoList);
        int insertLength = newMetaDataList.size();
        int i = 0;
        log.info("批量插入元数据开始, 总条数:{}", insertLength);
        while (insertLength > sysConfig.getBatchSize()) {
            long begin = DateUtil.current();
            metaDataService.batchInsert(newMetaDataList.subList(i, i + sysConfig.getBatchSize()));
            log.info("批量插入元数据, 开始条数:{}, 结束条数:{}, 耗时:{}ms", i, i + sysConfig.getBatchSize(), DateUtil.current() - begin);
            i = i + sysConfig.getBatchSize();
            insertLength = insertLength - i;

        }
        if (insertLength > 0) {
            long begin = DateUtil.current();
            metaDataService.batchInsert(newMetaDataList.subList(i, i + insertLength));
            log.info("批量插入元数据, 开始条数:{}, 结束条数:{}, 耗时:{}ms", i, i + insertLength, DateUtil.current() - begin);
        }
    }

    /**
     * 批量插入元数据详情
     *
     * @param metaDataId 元数据id
     * @param columnList 需更新数据
     */
    private void batchInsertMetaDataDetails(String metaDataId, List<MetaDataColumnDetailDto> columnList) {
        if (0 == columnList.size()) {
            return;
        }
        List<MetaDataDetails> newMetaDataDetailsList = this.getMetaDataDetailsList(metaDataId, columnList);
        int insertLength = newMetaDataDetailsList.size();
        int i = 0;
        log.info("批量插入元数据详情开始，总条数:{}", insertLength);
        while (insertLength > sysConfig.getBatchSize()) {
            long begin = DateUtil.current();
            metaDataDetailsService.batchInsert(newMetaDataDetailsList.subList(i, i + sysConfig.getBatchSize()));
            log.info("批量插入元数据详情，开始条数:{}, 结束条数:{}, 耗时:{}ms", i, i + sysConfig.getBatchSize(), DateUtil.current() - begin);
            i = i + sysConfig.getBatchSize();
            insertLength = insertLength - i;

        }
        if (insertLength > 0) {
            long begin = DateUtil.current();
            metaDataDetailsService.batchInsert(newMetaDataDetailsList.subList(i, i + insertLength));
            log.info("批量插入元数据详情，开始条数:{}, 结束条数:{}, 耗时:{}ms", i, i + insertLength, DateUtil.current() - begin);
        }
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
            metaDataList.add(metaData);
        }
        return metaDataList;
    }

    /**
     * 清空旧数据
     */
    private void delOldData() {
        //删除元数据
        metaDataService.truncate();
        //删除元数据详情
        metaDataDetailsService.truncate();
    }
}
