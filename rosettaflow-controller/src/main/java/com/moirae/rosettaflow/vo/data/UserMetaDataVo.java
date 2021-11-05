package com.moirae.rosettaflow.vo.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moirae.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/26
 * @description 用户授权元数据信息
 */
@Data
@ApiModel
public class UserMetaDataVo {

    @ApiModelProperty(value = "用户数据表ID")
    private Long id;

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "资源所属组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "资源所属组织名称")
    private String identityName;

    @ApiModelProperty(value = "资源所属组织中调度服务的 nodeId")
    private String nodeId;

    @ApiModelProperty(value = "授权方式:0-按次数/按时间, 1-按时间, 2-按次数, 3-永久")
    private Byte authType;

    @ApiModelProperty(value = "授权值:按次数单位为（次）")
    private Integer authValue;

    @ApiModelProperty(value = "授权值:以次数方式申请则显示次数，以时间方式申请则显示时间")
    private String authValueStr;

    @ApiModelProperty(value = "授权开始时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date authBeginTime;

    @ApiModelProperty(value = "授权结束时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date authEndTime;

    @ApiModelProperty(value = "授权状态: 0-等待审核中, 1-审核通过, 2-审核拒绝")
    private Byte authStatus;

    @ApiModelProperty(value = "发起授权申请的时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date applyTime;
    /**
     * 审核授权申请的时间
     */
    @ApiModelProperty(value = "审核授权申请的时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date auditTime;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    private Byte status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date updateTime;

    @ApiModelProperty(value = "元数据名称")
    private String dataName;
}
