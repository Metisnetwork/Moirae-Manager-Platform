package com.datum.platform.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author liushuyu
 * @date 2022/10/11 11:02
 * @desc
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "待签名工作流信息")
public class WorkflowUnsignedWorkflowDto extends WorkflowVersionKeyDto {

    @ApiModelProperty(value = "发起任务选择的凭证列表", required = true)
    private List<Long> credentialIdList;
}