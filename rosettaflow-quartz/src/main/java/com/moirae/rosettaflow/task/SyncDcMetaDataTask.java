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

//    @Scheduled(fixedDelay = 5 * 1000)
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
        tokenList.add(create("lat1px3vf0rqnufqha99gcl4gugztausuz67ghwdym"));
        tokenList.add(create("lat1qfm3mfchyl7pfkpeyqu7n0tldll2qd9q6l3jg4"));
        tokenList.add(create("lat1kaynntyhqaggm95maufgsx8rrlhz8fas9dc9vw"));
        tokenList.add(create("lat1axqxxcsp8e5nqz6xmpfysu0r72m43kwxx8zsqm"));
        tokenList.add(create("lat1s02hguahwyqg3n5eflzf38mz8kn6un7yaa6g5r"));
        tokenList.add(create("lat1hmngzrqef4fukl3e9g7gh04js3yv6cjquyp7m0"));
        tokenList.add(create("lat1gn8sf983zlck8a0ff8qksss8msr8r75sh535ze"));
        tokenList.add(create("lat1s60zk6guhwfl68lx340gsx064rgjh2wf9ajzml"));
        tokenList.add(create("lat1r5h8l2lereura9qla6kagf9eyj6lk8d5h7rucz"));
        tokenList.add(create("lat1j8y9xvte72dyg49l7shje3vxntkd79azuw80mz"));
        tokenList.add(create("lat17v76ekg0czxe3q7em0wlqe54lzkertd5375g3p"));
        tokenList.add(create("lat1cra484tah4j80gcnf99xuy56gl0srttxeqvcxx"));
        tokenList.add(create("lat1p5wrdlla4aquc8m8xa95s9r4xnd3wyh5ecztlx"));
        tokenList.add(create("lat1mtl4dylql2ahgxnn9vrel084z7gpcegm8zuljf"));
        tokenList.add(create("lat1cljxtyuta7xj7ex4ldmeh4aj0pkr6vjnplju7u"));
        tokenList.add(create("lat1377ersdd9f33uxaku2f6ncytn4eleupyfd25ed"));
        tokenList.add(create("lat1055ussjcd3gu3wz5pz3zyhcsaa0fm395fnsw2q"));
        tokenList.add(create("lat1hlne24asesu966863cvy6p50s0lp2h8my63h7j"));
        tokenList.add(create("lat1jgmfza3lpw8tlnk543qk0gdejryjjj98kucmsg"));
        tokenList.add(create("lat1u4pg37fjp973pnyermxkqq9g2r2qwr93te24ad"));
        tokenList.add(create("lat1yk9xdtlulxfk4yv8cwqqce2t23nn4sdnuap95l"));
        tokenList.add(create("lat1m3q2jmdjr0vtjc5pphdna9w2g04rgv77t7wytf"));
        tokenList.add(create("lat10fgqwrrta4ny3ry6qrzxdz7l4zw0d6uw6kx20w"));
        tokenList.add(create("lat18ma2qfpd3y6ennnee9w06x9jl5psrk05uk8z2w"));
        tokenList.add(create("lat1d75vd37065j6wmn7mzkk7a3ujugd95n5l0dp9q"));
        tokenList.add(create("lat189yc8hac8dsw8tarhkzxa7sxr5kmf6qn743srq"));

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
