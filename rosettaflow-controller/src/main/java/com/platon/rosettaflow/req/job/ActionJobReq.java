package com.platon.rosettaflow.req.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

/**
 * @author juzix
 * @date 2021/8/26
 * @description 操作作业
 */
@Data
@ApiModel(value = "操作作业请求")
public class ActionJobReq {

    @ApiModelProperty(value = "作业ID", required = true)
    @NotNull(message = "{job.id.notNull}")
    @Positive(message = "{job.id.positive}")
    private Long id;

    @ApiModelProperty(value = "操作作业类型: 1、暂停 2、重启", required = true)
    @NotNull(message = "工作流ID不能为空")
    private Byte actionType;


}
