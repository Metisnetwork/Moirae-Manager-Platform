package com.platon.rosettaflow.req.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/26
 * @description 修改作业请求对象
 */
@Data
@ApiModel(value = "修改作业请求")
public class EditJobReq {

    @ApiModelProperty(value = "作业ID", required = true)
    @NotNull(message = "作业ID不能为空")
    @Positive(message = "作业ID错误")
    private Long id;

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "工作流ID不能为空")
    @Positive(message = "工作流ID错误")
    private Long workflowId;

    @ApiModelProperty(value = "作业名称", required = true)
    @NotBlank(message = "作业名称不能为空")
    private String name;

    @ApiModelProperty(value = "作业描述")
    private String desc;

    @ApiModelProperty(value = "是否重复：0-否,1-是", required = true)
    private Byte repeatFlag;

    @ApiModelProperty(value = "重复间隔，单位分钟")
    private Integer repeatInterval;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}
