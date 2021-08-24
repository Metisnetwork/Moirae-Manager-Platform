package com.platon.rosettaflow.grpc.metadata.req.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/10
 * @description 元文件详情主体
 */
@Data
public class MetaDataDetailDto {
    /**
     * 源数据的摘要内容
     */
    private MetaDataSummaryDto metaDataSummary;
    /**
     * 列的描述详情
     */
    private List<MetaDataColumnDetailDto> metaDataColumnDetailDtoList;
}
