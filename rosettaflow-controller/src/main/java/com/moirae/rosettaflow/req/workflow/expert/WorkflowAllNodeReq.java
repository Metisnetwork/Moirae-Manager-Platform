package com.moirae.rosettaflow.req.workflow.expert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 保存工作流详情请求对象
 *
 * @author hudenian
 * @date 2021/9/28
 */
@Data
@ApiModel
public class WorkflowAllNodeReq {


    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流节点列表", required = true)
    List<WorkflowNodeReq> workflowNodeReqList;
}
