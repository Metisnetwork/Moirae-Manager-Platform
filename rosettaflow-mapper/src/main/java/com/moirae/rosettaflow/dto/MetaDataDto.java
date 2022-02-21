package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.enums.MetaDataFileTypeEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MetaDataDto implements Serializable {

    /**
     * 元数据ID,hash
     */
    private String metaDataId;

    /**
     * 数据文件ID,hash
     */
    private String originId;

    /**
     * 组织身份ID
     */
    private String identityId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 文件后缀/类型, 0:未知; 1:csv
     */
    private MetaDataFileTypeEnum fileType;

    /**
     * 行业名称
     */
    private String industry;

    /**
     * 文件大小(字节)
     */
    private Long size;

    /**
     * 数据行数(不算title)
     */
    private Integer rows;

    /**
     * 数据列数
     */
    private Integer columns;

    /**
     * 发布时间，精确到毫秒
     */
    private Date publishedAt;

    /**
     * 是否带标题
     */
    private Boolean hasTitle;

    /**
     * 数据描述
     */
    private String remarks;

    /**
     * 元数据的状态 (0: 未知; 1: 还未发布的新表; 2: 已发布的表; 3: 已撤销的表)
     */
    private MetaDataStatusEnum status;

    /**
     * (状态)修改时间
     */
    private Date updateAt;

    /**
     * 参与任务的数据
     */
    private Integer taskCount;
}
