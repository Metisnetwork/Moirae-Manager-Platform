package com.platon.rosettaflow.vo.workflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/30
 * @description 工作流详情响应对象
 */
@Data
@ApiModel
public class WorkflowDetailVo {

    @ApiModelProperty(value = "工作流ID")
    private Long id;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "工作流描述")
    private String workflowDesc;

    @ApiModelProperty(value = "运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败")
    private Byte runStatus;

    @ApiModelProperty(value = "节点数")
    private Integer nodeNumber;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date createTime;

    @ApiModelProperty(value = "工作流节点列表")
    private List<WorkflowNodeVo> workflowNodeVoList;

}
