package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.service.dto.workflow.common.DataInputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel
public class PTTrainingInputDto {

    @ApiModelProperty(value = "发起方的组织的身份标识Id", required = true)
    @NotBlank(message = "{workflow.node.sender.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "向导模式下训练元数据输入")
    private DataInputDto dataInput;

    @ApiModelProperty(value = "算力提供方式 0-随机 1-指定", required = true)
    private Integer powerType;

    @ApiModelProperty(value = "如果指定算力，算力提供组织")
    private String powerIdentityId;
}
