package com.datum.platform.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "最新的任务列表")
public class LatestTaskVo {

    @ApiModelProperty(value = "任务ID,hash")
    private String id;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务发起时间，精确到毫秒")
    private Date createAt;

    @ApiModelProperty(value = "任务计算开始时间，精确到毫秒")
    private Date startAt;

    @ApiModelProperty(value = "任务计算结束时间，精确到毫秒")
    private Date endAt;

    @ApiModelProperty(value = "发起方组织的身份标识id")
    private String identityId;

    @ApiModelProperty(value = "发起方组织的身份名称")
    private String nodeName;

    @ApiModelProperty(value = "发起方组织机构图像url")
    private String imageUrl;
}
