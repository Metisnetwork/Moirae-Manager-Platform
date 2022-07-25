package com.datum.platform.service.dto.workflow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WorkflowVersionKeyAndCredentialIdListDto extends WorkflowVersionKeyDto {

    @ApiModelProperty(value = "发起任务选择的凭证列表", required = true)
    private List<Long> credentialIdList;
}
