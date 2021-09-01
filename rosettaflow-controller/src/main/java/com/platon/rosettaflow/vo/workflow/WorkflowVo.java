package com.platon.rosettaflow.vo.workflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.platon.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工作流列表响应对象
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowVo {

    @ApiModelProperty(value = "工作流ID")
    private Long id;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date createTime;

    @ApiModelProperty(value = "创建者")
    private String userName;

}
