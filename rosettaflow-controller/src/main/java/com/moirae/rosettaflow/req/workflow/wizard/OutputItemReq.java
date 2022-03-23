package com.moirae.rosettaflow.req.workflow.wizard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 输出请求对象
 * @author hudenian
 * @date 2021/9/28
 */
@Data
@ApiModel(value = "工作流节点输出请求对象")
public class OutputItemReq {

    @ApiModelProperty(value = "结算输入方组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "存储形式: 1-明文，2:密文")
    private Integer storePattern;
}
