package com.platon.rosettaflow.vo.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.platon.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据详情
 */
@Data
@ApiModel
public class MetaDataDetailVo {

    @ApiModelProperty(value = "元数据表ID")
    private Long id;

    @ApiModelProperty(value = "资源所属组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "资源所属组织名称")
    private String identityName;

    @ApiModelProperty(value = "资源所属组织中调度服务的 nodeId")
    private String nodeId;

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "源文件ID")
    private String fileId;

    @ApiModelProperty(value = "元数据名称|数据名称 (表名)")
    private String dataName;

    @ApiModelProperty(value = "元数据的描述 (摘要)")
    private String dataDesc;

    @ApiModelProperty(value = "源文件存放路径")
    private String filePath;

    @ApiModelProperty(value = "源文件的行数")
    private Integer rows;

    @ApiModelProperty(value = "源文件的列数")
    private Integer columns;

    @ApiModelProperty(value = "源文件的大小 (单位: byte)")
    private Long size;

    @ApiModelProperty(value = "源文件类型: 0-未知，1- CSV类型")
    private Byte fileType;

    @ApiModelProperty(value = "是否带标题,0表示不带，1表示带标题")
    private Byte hasTitle;

    @ApiModelProperty(value = "元数据所属行业")
    private String industry;

    @ApiModelProperty(value = "元数据的状态 (create: 还未发布的新表; release: 已发布的表; revoke: 已撤销的表)")
    private Byte dataStatus;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    private Byte status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date updateTime;

    @ApiModelProperty(value = "授权方式: 0-按次数/按时间,1-按时间, 2-按次数, 3-永久")
    private Byte authType;
}
