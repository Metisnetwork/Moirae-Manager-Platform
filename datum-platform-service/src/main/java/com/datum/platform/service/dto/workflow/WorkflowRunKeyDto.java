package com.datum.platform.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "编辑工作流请求对象")
public class WorkflowRunKeyDto {

    @ApiModelProperty(value = "工作流运行记录id")
    private Long workflowRunId;

}
