package com.moirae.rosettaflow.req.workflow;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "工作流运行记录请求参数")
public class RunningRecordReq extends CommonPageReq {

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "项目id", required = true)
    @NotNull(message = "{project.id.notNull}")
    @Positive(message = "{project.id.positive}")
    private Long projectId;
}
