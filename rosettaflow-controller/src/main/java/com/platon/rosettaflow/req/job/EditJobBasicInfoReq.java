package com.platon.rosettaflow.req.job;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/8/26
 * @description 修改作业基本信息请求对象
 */
@Data
@ApiModel(value = "修改作业基本信息请求对象")
public class EditJobBasicInfoReq {
    @ApiModelProperty(value = "作业ID", required = true)
    @NotNull(message = "{job.id.notNull}")
    @Positive(message = "{job.id.positive}")
    private Long id;

    @ApiModelProperty(value = "作业名称", required = true)
    @NotBlank(message = "{job.name.notNull}")
    private String name;

    @ApiModelProperty(value = "作业描述")
    private String desc;
}
