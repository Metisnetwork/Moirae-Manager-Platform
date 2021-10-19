package com.platon.rosettaflow.req.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author juzix
 */
@Data
@ApiModel(value = "删除作业请求参数")
public class DeleteBatchJobReq {
    @ApiModelProperty(value = "作业id集合", required = true)
    private List<Long> jobIds;
}
