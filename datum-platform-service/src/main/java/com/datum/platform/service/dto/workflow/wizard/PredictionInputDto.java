package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.service.dto.model.ModelDto;
import com.datum.platform.service.dto.workflow.common.DataInputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel
public class PredictionInputDto {

    @ApiModelProperty(value = "发起方的组织的身份标识Id", required = true)
    @NotBlank(message = "{task.sender.NotBlank}")
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
    @Size(message = "{task.dataInput.size.equal.2}", min = 2, max = 2)
    @Valid
    private List<DataInputDto> item;
}
