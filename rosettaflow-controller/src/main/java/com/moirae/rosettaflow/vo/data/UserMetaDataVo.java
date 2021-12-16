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

    @ApiModelProperty(value = "用户授权数据表Id")
    private Long id;

    @ApiModelProperty(value = "元数据metaDataId")
    private String metaDataId;

    @ApiModelProperty(value = "元数据表Id")
    private Long metaDataPkId;

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

    @ApiModelProperty(value = "授权状态: -1-未知(1.未登录故获取不到授权状态 2.用户未申请使用元数据), 0-已申请(待审核), 1-已授权, 2-已拒绝, 3-已撤销, 4-已失效")
    private Byte authStatus;

    @ApiModelProperty(value = "数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的or被拒绝的>;)")
    private Byte authMetadataState;

    @ApiModelProperty(value = "转换后展示状态: 0-申请中, 1-已授权, 2-已拒绝, 3-已失效")
    private Byte authStatusShow;

    @ApiModelProperty(value = "发起授权申请的时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date applyTime;
    /**
     * 审核授权申请的时间
     */
    @ApiModelProperty(value = "审核授权申请的时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date auditTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date updateTime;

    @ApiModelProperty(value = "元数据名称")
    private String dataName;
}
