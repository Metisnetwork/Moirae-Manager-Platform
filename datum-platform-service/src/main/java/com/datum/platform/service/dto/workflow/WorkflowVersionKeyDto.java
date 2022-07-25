package com.datum.platform.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowVersionKeyDto extends WorkflowKeyDto {

    @ApiModelProperty(value = "工作流最新版本号")
    private Long workflowVersion;
}
