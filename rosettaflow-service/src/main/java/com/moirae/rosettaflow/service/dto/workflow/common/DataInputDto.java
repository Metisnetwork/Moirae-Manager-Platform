package com.moirae.rosettaflow.service.dto.workflow.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "工作流节点输入请求对象")
public class DataInputDto {

    @ApiModelProperty(value = "数据提供方组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "元数据id")
    @NotNull(message = "{node.input.data.table.id.NotNull}")
    private String metaDataId;

    @ApiModelProperty(value = "ID列(列索引)")
    @NotNull(message = "{node.identity.name.id.column}")
    private Long keyColumn;

    @ApiModelProperty(value = "因变量（标签）, 训练时必填, 预测不需要")
    private Long dependentVariable;

    @ApiModelProperty(value = "数据字段ID,多个以‘,’分隔")
    @NotBlank(message = "{node.identity.name.NotBlank}")
    private String dataColumnIds;
}
