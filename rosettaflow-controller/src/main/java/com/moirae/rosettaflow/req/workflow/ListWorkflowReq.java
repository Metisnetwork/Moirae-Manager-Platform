package com.moirae.rosettaflow.req.workflow;

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
    @NotNull(message = "{project.id.notNull}")
    private Long projectId;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "当前页码, 默认第一页")
    @NotNull(message = "{page.number.notBlank}")
    @Positive(message = "{page.number.positive}")
    private Long current;

    @ApiModelProperty(value = "每页大小, 默认每页十条. 最小支持每页1条, 最大支持每页1000条")
    @NotNull(message = "{page.number.notBlank}")
    @Min(value = 1L, message = "{each.page.row.min}")
    @Max(value = 1000L, message = "{each.page.row.max}")
    private Long size;


}
