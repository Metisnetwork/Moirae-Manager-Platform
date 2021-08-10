package com.platon.rosettaflow.grpc.metaData.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/10
 * @description 列的描述详情
 */
@Data
public class MetaDataColumnDetailDto {
    /**
     * 列的索引
     */
    private Integer index;
    /**
     * 列名
     */
    private String name;
    /**
     * 列类型
     */
    private String type;
    /**
     * 列大小(单位: byte)
     */
    private Integer size;
    /**
     * 列描述
     */
    private String comment;
}
