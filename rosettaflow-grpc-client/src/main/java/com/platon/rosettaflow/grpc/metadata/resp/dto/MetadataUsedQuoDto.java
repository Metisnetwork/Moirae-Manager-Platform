package com.platon.rosettaflow.grpc.metadata.resp.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/9/10
 * @description 对应数据授权信息中元数据的使用实况
 */
@Data
public class MetadataUsedQuoDto {
    /**
     * 元数据的使用方式类型枚举
     * 0-未定义类型
     * 1-依照时间段来使用
     * 2-依照次数来使用
     */
    private Integer metadataUsageType;
    /**
     * 是否已过期 (当 usage_type 为 1 时才需要的字段)
     */
    private boolean expire;
    /**
     * 已经使用的次数 (当 usage_type 为 1 时才需要的字段)
     */
    private Integer usedTimes;
}
