package com.moirae.rosettaflow.req.workflow;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "工作流运行记录请求参数")
public class RunningRecordReq extends CommonPageReq {

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;
}
