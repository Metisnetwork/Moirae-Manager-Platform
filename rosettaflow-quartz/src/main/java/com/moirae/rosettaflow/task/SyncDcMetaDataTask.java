package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataSummaryDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcMetaDataService;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import com.moirae.rosettaflow.mapper.domain.Token;
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
import java.util.*;

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

        //TODO
        List<Token> tokenList = new ArrayList<>();
        tokenList.add(create("0x3a3b85bfc6b7b8435d713dca5ce308b9c4abe430"));
        tokenList.add(create("0x282875fd9579367c44a8b2666d65f75f8c589fdb"));
        tokenList.add(create("0xfc23df226ca45f9a2c527d8313e27395964060fa"));
        tokenList.add(create("0xb74c8525dfb15598687c80be6615fb243c2eeb25"));
        tokenList.add(create("0x148d92fc65b04c60840793be11b9f1b558e6eb9e"));
        tokenList.add(create("0xbb54ec09ec1433fa48716effc94bbe9a950aecef"));
        tokenList.add(create("0x90d308f5e6ab2d087983ac4d5c8ced533ed7681a"));
        tokenList.add(create("0xdd4f9e77c206c64343140e25f5bf56dec6d81750"));
        tokenList.add(create("0xfc4239a8dfe02c0d6db4a2b0c9ce8d11fd80b73c"));
        tokenList.add(create("0x33f919a0f6312bfd225f797a4808683958660e3a"));
        tokenList.add(create("0xab94a2338ca24d05e2cc6318ecbd083eabbe473c"));
        tokenList.add(create("0x4ab735a85751534ce7d2bf733bc083232573fb06"));
        tokenList.add(create("0xe1b66c3ef7ca24e9b37b0ec38868fdb67881696f"));
        tokenList.add(create("0x1981e3ab9dd60eae70ae83d3d1b236c2662eae8a"));
        tokenList.add(create("0xb9ef5fd080839d3eb04809c0f69db709dd9b5f69"));
        tokenList.add(create("0xfa0bea7347b9b98e068528da5826cf45a9c6075e"));
        tokenList.add(create("0xcacc4ca37e0cd5d162455a753cf07bcdbc26281b"));
        tokenList.add(create("0x1e19040bae09c01d06e82ebd83c801308959daed"));
        tokenList.add(create("0xc91ef95ac29cfafce0b0b714051e5add99b64c87"));
        tokenList.add(create("0x8e801527c44929da704b7cd3406142f5391fe1f7"));
        tokenList.add(create("0x2fa1f9217fead87874ff63715c29af10d78f741e"));
        tokenList.add(create("0x4be1ae0984ddb224c863c1e20fec05c0b86c7d3d"));
        tokenList.add(create("0xa4f375aa5b51de1510709c8fdfbe20cc4aa8790c"));
        tokenList.add(create("0xb861eea15a711c19574dde3c582278e4e06a6482"));
        tokenList.add(create("0x2d0602fd33ff61e24a886060c13a3c619aa6b61e"));
        tokenList.add(create("0x15950be327f74885f64ec3c5f0367ea643a4a6c9"));

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
        metaDataService.batchReplace(metaDataList, metaDataColumnList, tokenList);
    }

    private Token create(String address){
        Token token = new Token();
        token.setAddress( AddressChangeUtils.convert0xAddress(address));
        return token;
    }
}
