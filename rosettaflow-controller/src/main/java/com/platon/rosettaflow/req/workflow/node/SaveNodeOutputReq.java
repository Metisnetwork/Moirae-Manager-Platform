package com.platon.rosettaflow.req.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 输入请求对象
 * @author hudenian
 * @date 2021/8/31
 */
@Data
@ApiModel(value = "输入请求对象")
public class SaveNodeOutputReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.node.id.notNull}")
    @Positive(message = "{workflow.node.id.positive}")
    private Long workflowNodeId;

    @ApiModelProperty(value = "组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    @NotBlank(message = "{node.identity.name.NotBlank}")
    private String identityName;

    @ApiModelProperty(value = "是否发起方: 0-否,1-是(默认为1)")
    @NotBlank(message = "{node.identity.name.NotBlank}")
    private String savePartnerFlag;

    @ApiModelProperty(value = "存储形式: 1-明文，2:密文(默认为1)")
    private String storePattern;

}
