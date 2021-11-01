package com.moirae.rosettaflow.req.job;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


/**
 * @author juzix
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "作业列表请求参数")
public class ListJobReq extends CommonPageReq {
    @ApiModelProperty(value = "作业名称")
    private String jobName;

    @ApiModelProperty(value = "项目id", required = true)
    @NotNull(message = "{project.id.notNull}")
    @Positive(message = "{project.id.positive}")
    private Long projectId;
}
