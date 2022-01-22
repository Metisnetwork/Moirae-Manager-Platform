package com.moirae.rosettaflow.vo.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.moirae.rosettaflow.vo.workflow.node.NodeTaskResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class RunningRecordItemVo {

    @ApiModelProperty(value = "工作流运行记录明细id")
    private Long id;

    @ApiModelProperty(value = "工作流运行记录id")
    private Long workflowRunId;

    @ApiModelProperty(value = "节点在工作流中序号,从1开始")
    private Integer nodeStep;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "运行状态: 0-未开始 1-运行中,2-运行成功,3-运行失败")
    private Byte runStatus;

    @ApiModelProperty(value = "任务ID,底层处理完成后返回")
    private String taskId;

    @ApiModelProperty(value = "任务处理结果描述")
    private String runMsg;

    @ApiModelProperty(value = "工作流节点需要的模型id")
    private Long modelId;

    @ApiModelProperty(value = "任务产生的文件")
    private List<NodeTaskResultVo> taskResultList;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
