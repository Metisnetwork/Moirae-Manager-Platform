package com.platon.rosettaflow.vo.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询关联工作流响应对象
 *
 * @author houz
 * @date 2021/8/26
 */
@Data
@ApiModel(value = "查询关联工作流响应对象")
public class QueryWorkflowVo {

    @ApiModelProperty(value = "工作流ID")
    private Long id;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

}
