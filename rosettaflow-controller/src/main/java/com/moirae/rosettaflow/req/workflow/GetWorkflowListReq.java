package com.moirae.rosettaflow.req.workflow;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "任务列表查询")
public class GetWorkflowListReq extends CommonPageReq {

    @ApiModelProperty(value = "搜索关键字(工作流名称进行模糊查询)")
    private String keyword;

    @ApiModelProperty(value = "算法id")
    private String algorithmId;

    @ApiModelProperty(value = "时间的开始")
    private Date begin;

    @ApiModelProperty(value = "时间的结束")
    private Date end;
}
