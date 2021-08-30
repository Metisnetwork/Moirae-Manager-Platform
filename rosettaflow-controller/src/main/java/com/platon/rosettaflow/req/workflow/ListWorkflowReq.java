package com.platon.rosettaflow.req.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/8/30
 * @description 工作流列表请求参数
 */
@Data
@ApiModel(value = "工作流列表请求参数")
public class ListWorkflowReq {

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "项目ID不能为空")
    @Positive(message = "项目ID不能小于等于0")
    private Long projectId;

    @ApiModelProperty(value = "当前页码, 默认第一页")
    @Positive(message = "页码不能小于等于0")
    private Long current = 1L;

    @ApiModelProperty(value = "每页大小, 默认每页十条. 最小支持每页1条, 最大支持每页1000条")
    @Min(value = 1L, message = "每页大小不能小于1")
    @Max(value = 1000L, message = "每页大小不能超过1000")
    private Long size = 10L;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;
}
