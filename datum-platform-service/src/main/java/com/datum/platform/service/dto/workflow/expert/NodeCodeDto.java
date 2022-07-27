package com.datum.platform.service.dto.workflow.expert;

import com.datum.platform.service.dto.alg.AlgVariableV2Dto;
import com.datum.platform.service.dto.workflow.common.CodeDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 专家模式节点的代码明细
 */
@Data
@ApiModel(value = "专家模式节点的代码明细")
public class NodeCodeDto {

    @ApiModelProperty(value = "变量", required = true)
    private List<AlgVariableV2Dto> variableList;

    @ApiModelProperty(value = "代码", required = true)
    private CodeDto code;
}
