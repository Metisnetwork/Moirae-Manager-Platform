package com.platon.rosettaflow.vo.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 查询关联工作流响应对象
 * @author houz
 * @date 2021/8/26
 */
@Data
@ApiModel(value = "查询关联工作流响应对象")
public class QueryWorkflowVo {

    @ApiModelProperty(value = "工作流ID")
    private Long workflowId;

    @ApiModelProperty(value = "工作流名称")
    private Long workflowName;

}
