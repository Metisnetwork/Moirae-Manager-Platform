package com.moirae.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowKeyVo {

    @ApiModelProperty(value = "工作流ID")
    private Long workflowId;

    @ApiModelProperty(value = "工作流最新版本号")
    private Integer workflowVersion;
}
