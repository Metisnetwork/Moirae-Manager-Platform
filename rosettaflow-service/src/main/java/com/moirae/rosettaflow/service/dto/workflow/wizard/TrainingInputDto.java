package com.moirae.rosettaflow.service.dto.workflow.wizard;

import com.moirae.rosettaflow.service.dto.workflow.common.DataInputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(value = "工作流节点输入请求对象")
public class TrainingInputDto {

    @ApiModelProperty(value = "发起方的组织的身份标识Id", required = true)
    @NotBlank(message = "{workflow.node.sender.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "是否需要做psi")
    private Boolean isPsi;

    @ApiModelProperty(value = "元数据输入")
    private List<DataInputDto> item;
}
