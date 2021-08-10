package com.platon.rosettaflow.grpc.metaData.dto;

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
    private Integer size;
    /**
     * 源文件的类型 (目前只有 csv)
     */
    private String fileType;
    /**
     * 源文件是否包含标题
     */
    private Boolean hasTitle;
    /**
     * 元数据的状态 (create: 还未发布的新表; release: 已发布的表; revoke: 已撤销的表)
     */
    private String state;
}
