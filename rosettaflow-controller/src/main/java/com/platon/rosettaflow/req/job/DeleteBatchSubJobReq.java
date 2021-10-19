package com.platon.rosettaflow.req.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;


/**
 * @author juzix
 */
@Data
@ApiModel(value = "删除子作业请求参数")
public class DeleteBatchSubJobReq {
    @ApiModelProperty(value = "子作业id集合", required = true)
    private List<Long> subJobIds;
}
