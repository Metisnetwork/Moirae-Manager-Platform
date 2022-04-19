package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
import com.moirae.rosettaflow.grpc.client.GrpcMetaDataServiceClient;
import com.moirae.rosettaflow.grpc.service.GetGlobalMetadataDetail;
import com.moirae.rosettaflow.grpc.service.types.MetadataSummary;
import com.moirae.rosettaflow.grpc.service.types.Organization;
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
import org.apache.commons.lang3.StringUtils;
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
public class SyncDcMetaDataTask {

    @Resource
    private GrpcMetaDataServiceClient grpcMetaDataService;

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
                                .getInformation()
                                .getMetadataSummary()
                                .getUpdateAt();
                    });
        } catch (Exception e) {
            log.error("元数据信息同步,从net同步元数据失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("元数据信息同步结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    /**
     * @param getGlobalMetadataDetailList 需更新数据
     */
    private void batchUpdateMetaData(List<GetGlobalMetadataDetail> getGlobalMetadataDetailList) {
        List<MetaData> metaDataList = new ArrayList<>();
        List<MetaDataColumn> metaDataColumnList = new ArrayList<>();
        List<Token> tokenList = new ArrayList<>();

        getGlobalMetadataDetailList.stream().forEach(item -> {
            Organization organization = item.getOwner();
            MetadataSummary information = item.getInformation().getMetadataSummary();
            JSONObject metadataOption = JSONObject.parseObject(information.getMetadataOption());
            MetaData metaData = new MetaData();
            metaData.setMetaDataId(information.getMetadataId());
            metaData.setFileName(information.getMetadataName());
            metaData.setMetaDataType(information.getMetadataTypeValue());
            metaData.setIdentityId(organization.getIdentityId());
            metaData.setRemarks(information.getDesc());
            metaData.setLocationType(information.getLocationTypeValue());
            metaData.setFileType(MetaDataFileTypeEnum.find(information.getDataTypeValue()));
            metaData.setIndustry(information.getIndustry());
            metaData.setStatus(MetaDataStatusEnum.find(information.getStateValue()));
            metaData.setPublishedAt(new Date(information.getPublishAt()));
            metaData.setUpdateAt(new Date(information.getUpdateAt()));
            metaData.setNonce(information.getNonce());
            metaData.setAllowExpose(information.getAllowExpose());
            metaData.setTokenAddress(information.getTokenAddress());
            metaData.setOriginId(metadataOption.getString("originId"));
            metaData.setFilePath(metadataOption.getString("dataPath"));
            metaData.setSize(metadataOption.getLong("size"));
            metaData.setRows(metadataOption.getInteger("rows"));
            metaData.setColumns(metadataOption.getInteger("columns"));
            metaData.setHasTitle(metadataOption.getBoolean("hasTitle"));
            metaDataList.add(metaData);

            JSONArray metadataColumns = metadataOption.getJSONArray("metadataColumns");
            for (int i = 0; i < metadataColumns.size(); i++) {
                JSONObject metadataColumn = metadataColumns.getJSONObject(i);
                MetaDataColumn metaDataColumn = new MetaDataColumn();
                metaDataColumn.setMetaDataId(metaData.getMetaDataId());
                metaDataColumn.setColumnIdx(metadataColumn.getInteger("index"));
                metaDataColumn.setColumnName(metadataColumn.getString("name"));
                metaDataColumn.setColumnType(metadataColumn.getString("type"));
                metaDataColumn.setColumnSize(metadataColumn.getInteger("size"));
                metaDataColumn.setRemarks(metadataColumn.getString("comment"));
                metaDataColumnList.add(metaDataColumn);
            }

            if(StringUtils.isNotBlank(information.getTokenAddress())){
                tokenList.add(create(information.getTokenAddress()));
            }
        });
        metaDataService.batchReplace(metaDataList, metaDataColumnList, tokenList);
    }

    private Token create(String address){
        Token token = new Token();
        token.setAddress(AddressChangeUtils.convert0xAddress(address));
        return token;
    }
}
