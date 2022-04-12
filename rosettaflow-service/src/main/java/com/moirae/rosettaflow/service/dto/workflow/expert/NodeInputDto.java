package com.moirae.rosettaflow.service.dto.workflow.expert;

import com.moirae.rosettaflow.service.dto.model.ModelDto;
import com.moirae.rosettaflow.service.dto.workflow.common.DataInputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(value = "工作流详情请求对象")
public class NodeInputDto {

    @ApiModelProperty(value = "发起方的组织的身份标识Id", required = true)
    @NotBlank(message = "{workflow.node.sender.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "数据输入", required = true)
    private List<DataInputDto> dataInputList;

    @ApiModelProperty(value = "是否需要输入模型: 0-否，1:是")
    private Boolean inputModel;

    @ApiModelProperty(value = "工作流当前节点模型ID")
    private ModelDto model;

    @ApiModelProperty(value = "是否需要做psi")
    private Boolean isPsi;
}
