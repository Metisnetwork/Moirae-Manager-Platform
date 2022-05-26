package com.datum.platform.service.dto.workflow.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 工作流输出明细
 */
@Data
@ApiModel
public class OutputDto {

    @ApiModelProperty(value = "结算输入方组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private List<String> identityId;

    @ApiModelProperty(value = "存储形式: 1-明文，2:密文")
    private Integer storePattern;
}
