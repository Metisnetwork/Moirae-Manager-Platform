package com.moirae.rosettaflow.req.workflow;

import com.moirae.rosettaflow.req.workflow.expert.WorkflowNodeReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@ApiModel
public class SettingWorkflowOfExpertModeReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流最新版本号")
    private Integer workflowVersion;

    @ApiModelProperty(value = "工作流所有节点列表")
    List<WorkflowNodeReq> workflowNodeReqList;
}
