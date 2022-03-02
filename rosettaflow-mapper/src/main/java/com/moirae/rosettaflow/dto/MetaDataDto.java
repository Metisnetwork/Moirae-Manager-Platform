package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.enums.*;
import io.swagger.annotations.ApiModelProperty;
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
    private String metaDataName;

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

    /**
     * 组织名称
     */
    private String nodeName;

    /**
     * 数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的>)")
     */
    private MetaDataAuthStatusEnum authStatus;

    /**
     * 审核结果，0：等待审核中；1：审核通过；2：审核拒绝
     */
    private MetaDataAuthOptionEnum auditOption;

    /**
     * 授权id
     */
    private String metaDataAuthId;

    /**
     * 授权申请时间，精确到毫秒
     */
    private Date applyAt;

    /**
     * 申请收集授权类型：(0: 未定义; 1: 按照时间段来使用; 2: 按照次数来使用)
     */
    private MetaDataAuthTypeEnum authType;

    /**
     * 授权开始时间(auth_type=1时)
     */
    private Date startAt;

    /**
     * 授权结束时间(auth_type=1时)
     */
    private Date endAt;

    /**
     * 授权次数(auth_type=2时)
     */
    private Integer times;

    /**
     * 使用次数(auth_type=2时)
     */
    private Integer usedTimes;
}
