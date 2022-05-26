package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.service.dto.model.ModelDto;
import com.datum.platform.service.dto.workflow.common.DataInputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel
public class PredictionInputDto {

    @ApiModelProperty(value = "发起方的组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "是否需要做psi")
    private Boolean isPsi;

    @ApiModelProperty(value = "是否需要输入模型: 0-否，1:是")
    private Boolean inputModel;

    @ApiModelProperty(value = "当前设置的算法id(查询时返回，提交不需要)")
    private Long algorithmId;

    @ApiModelProperty(value = "向导模式下预测的模型输入")
    private ModelDto model;

    @ApiModelProperty(value = "向导模式下预测元数据输入")
    private List<DataInputDto> item;
}
