package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.service.dto.workflow.common.DataInputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class PTTrainingInputDto {

    @ApiModelProperty(value = "发起方的组织的身份标识Id", required = true)
    @NotBlank(message = "{task.sender.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "向导模式下训练元数据输入")
    @NotNull(message = "{task.dataInput.object.NotNull}")
    private DataInputDto dataInput;

    @ApiModelProperty(value = "算力提供方式 0-随机 1-指定", required = true)
    @NotNull(message = "{task.powerType.NotNull}")
    private Integer powerType;

    @ApiModelProperty(value = "如果指定算力，算力提供组织")
    private String powerIdentityId;
}
