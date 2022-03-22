package com.moirae.rosettaflow.req.workflow.step;

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
public class OutputReq {

    @ApiModelProperty(value = "训练任务的结果输出方")
    private OutputItemReq training;

    @ApiModelProperty(value = "预测任务的结果输出方")
    private OutputItemReq prediction;

    @ApiModelProperty(value = "PSI的结果输出方")
    private OutputItemReq psi;
}
