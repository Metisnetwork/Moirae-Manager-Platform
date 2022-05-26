package com.datum.platform.service.dto.workflow.expert;

import com.datum.platform.service.dto.workflow.WorkflowVersionKeyDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专家模式节点id
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "专家模式节点id")
public class WorkflowNodeKeyDto extends WorkflowVersionKeyDto {

    @ApiModelProperty(value = "工作流节点序号,从1开始")
    private Integer nodeStep;
}
