package com.moirae.rosettaflow.req.workflow;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 批量删除工作流请求参数
 * @author houz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "批量删除工作流请求参数")
public class DeleteBatchReq {

    @ApiModelProperty(value = "工作流ID, 多个以逗号拼接", required = true)
    @NotBlank(message = "{workflow.ids.NotNull}")
    private String ids;
}
