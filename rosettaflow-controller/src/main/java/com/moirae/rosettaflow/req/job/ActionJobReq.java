package com.moirae.rosettaflow.req.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
    @NotNull(message = "{job.actionType.notNull}")
    @Range(min = 1, max = 2, message = "{job.actionType.type.error}")
    private Byte actionType;


}
