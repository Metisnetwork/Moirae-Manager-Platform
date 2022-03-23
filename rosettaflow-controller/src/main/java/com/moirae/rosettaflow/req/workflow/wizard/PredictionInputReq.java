package com.moirae.rosettaflow.req.workflow.wizard;

import com.moirae.rosettaflow.vo.model.ModelVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(value = "工作流节点输入请求对象")
public class PredictionInputReq {

    @ApiModelProperty(value = "发起方的组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "是否需要做psi")
    private Boolean isPsi;

    @ApiModelProperty(value = "是否需要输入模型: 0-否，1:是")
    private Integer inputModel;

    @ApiModelProperty(value = "工作流当前节点模型ID")
    private ModelVo modelVo;

    @ApiModelProperty(value = "元数据输入")
    private List<DataInputItemReq> item;
}
