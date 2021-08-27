package com.platon.rosettaflow.grpc.metadata.req.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 元数据怎么使用请求对象
 */
@Data
public class MetaDataUsageDto {

    /**
     * 元数据的使用方式 (0: 未定义; 1: 按照时间段来使用; 2: 按照次数来使用)
     */
    private Integer useType;
    /**
     * 使用开始时间 (当 usage_type 为 1 时才需要的字段)
     */
    private Long startAt;
    /**
     * 使用结束时间 (当 usage_type 为 1 时才需要的字段)
     */
    private Long endAt;
    /**
     * 使用次数 (当 usage_type 为 2 时才需要的字段)
     */
    private Long times;
}
