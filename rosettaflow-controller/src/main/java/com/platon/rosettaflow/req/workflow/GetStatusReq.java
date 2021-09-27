package com.platon.rosettaflow.req.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 获取工作流状态请求参数
 * @author houz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "获取工作流状态请求参数")
public class GetStatusReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long id;
}
