package com.platon.rosettaflow.req.job;

import com.platon.rosettaflow.req.CommonPageReq;
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
@ApiModel(value = "子作业列表请求参数")
public class ListSubJobReq extends CommonPageReq {
    @ApiModelProperty(value = "子作业id")
    private String subJobId;

    @ApiModelProperty(value = "作业id", required = true)
    @NotNull(message = "{job.id.notNull}")
    @Positive(message = "{job.id.positive}")
    private Long jobId;
}
