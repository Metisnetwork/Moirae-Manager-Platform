package com.datum.platform.service.dto.workflow.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 工作流输入明细
 */
@Data
@ApiModel
public class DataInputDto {

    @ApiModelProperty(value = "数据提供方组织的身份标识Id", required = true)
    @NotBlank(message = "{task.dataInput.object.identityId.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "元数据id")
    @NotBlank(message = "{task.dataInput.object.metaDataId.NotBlank}")
    private String metaDataId;

    @ApiModelProperty(value = "ID列(列索引)")
    @NotNull(message = "{task.dataInput.object.keyColumn.notNull}")
    @Positive(message = "{task.dataInput.object.keyColumn.positive}")
    private Long keyColumn;

    @ApiModelProperty(value = "因变量（标签）, 训练时必填, 预测不需要")
    private Long dependentVariable;

    @ApiModelProperty(value = "数据字段ID,多个以‘,’分隔")
    @NotBlank(message = "{node.identity.name.NotBlank}")
    private String dataColumnIds;
}
