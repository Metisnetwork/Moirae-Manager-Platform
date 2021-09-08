package com.platon.rosettaflow.req.job;

import com.platon.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author juzix
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "作业列表请求参数")
public class ListJobReq extends CommonPageReq {
    @ApiModelProperty(value = "作业名称")
    private String jobName;
}
