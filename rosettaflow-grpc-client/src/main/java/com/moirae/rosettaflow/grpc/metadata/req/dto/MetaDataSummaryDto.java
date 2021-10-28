package com.moirae.rosettaflow.grpc.metadata.req.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/10
 * @description 源数据的摘要内容
 */
@Data
public class MetaDataSummaryDto {
    /**
     * 元数据Id
     */
    private String metaDataId;
    /**
     * 源文件Id
     */
    private String originId;
    /**
     * 元数据名称 (表名)
     */
    private String tableName;
    /**
     * 元数据的描述 (摘要)
     */
    private String desc;
    /**
     * 源文件存放路径
     */
    private String filePath;
    /**
     * 源文件的行数
     */
    private Integer rows;
    /**
     * 源文件的列数
     */
    private Integer columns;
    /**
     * 源文件的大小 (单位: byte)
     */
    private Long size;
    /**
     * 0-未知 1-源文件的类型 (目前只有 csv)
     */
    private Integer fileType;
    /**
     * 源文件是否包含标题
     */
    private Boolean hasTitle;
    /**
     * 元数据所属行业
     */
    private String industry;
    /**
     * 元数据的状态 (0: 未知; 1: 还未发布的新表; 2: 已发布的表; 3: 已撤销的表)
     */
    private Integer dataState;
}
