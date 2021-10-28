package com.moirae.rosettaflow.vo.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moirae.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据列表
 */
@Data
@ApiModel
public class MetaDataVo {

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

    @ApiModelProperty(value = "元数据的状态 (1- 还未发布的新表; 2- 已发布的表; 3- 已撤销的表)")
    private Byte dataStatus;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    private Byte status;

    @ApiModelProperty(value = "授权状态: -1-未知(1.未登录故获取不到授权状态 2.用户未申请使用元数据),0-等待审核中, 1-审核通过, 2-审核拒绝")
    private Byte authStatus;

    @ApiModelProperty(value = "数据授权信息有效性状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的or被拒绝的>;)")
    private Byte authMetadataState;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date updateTime;
}
