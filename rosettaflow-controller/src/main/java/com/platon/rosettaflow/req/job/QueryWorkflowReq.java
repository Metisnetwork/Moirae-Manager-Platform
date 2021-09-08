package com.platon.rosettaflow.req.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 查询关联工作流请求对象
 * @author houz
 * @date 2021/8/26
 */
@Data
@ApiModel(value = "查询关联工作流请求对象")
public class QueryWorkflowReq {

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "{project.id.notNull}")
    @Positive(message = "{project.id.positive}")
    private Long projectId;

}
