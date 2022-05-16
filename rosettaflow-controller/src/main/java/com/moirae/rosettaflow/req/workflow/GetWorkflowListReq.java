package com.moirae.rosettaflow.req.workflow;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "工作流列表查询")
public class GetWorkflowListReq extends CommonPageReq {

    @ApiModelProperty(value = "创建模式:1-专家模式,2-向导模式")
    private Integer createMode;

    @ApiModelProperty(value = "搜索关键字(工作流名称进行模糊查询)")
    private String keyword;

    @ApiModelProperty(value = "算法id")
    private Long algorithmId;

    @ApiModelProperty(value = "时间的开始")
    private Date begin;

    @ApiModelProperty(value = "时间的结束")
    private Date end;

    public void setBegin(long begin){
        this.begin = new Date(begin);
    }

    public void setEnd(long end){
        this.end = new Date(end);
    }
}
