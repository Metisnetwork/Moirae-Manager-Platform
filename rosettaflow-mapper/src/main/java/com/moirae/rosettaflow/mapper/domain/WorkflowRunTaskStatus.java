package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 工作流任务运行状态
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_run_task_status")
public class WorkflowRunTaskStatus implements Serializable {


    /**
     * ID(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工作流运行状态ID
     */
    @TableField("workflow_run_id")
    private Long workflowRunId;

    /**
     * 工作流节点配置ID
     */
    @TableField("workflow_task_id")
    private Long workflowTaskId;

    /**
     * 节点在工作流中序号,从1开始
     */
    private Integer step;

    /**
     * 开始时间
     */
    @TableField("begin_time")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 运行状态: :0-未开始 1-运行中,2-运行成功,3-运行失败
     */
    @TableField("run_status")
    private Integer runStatus;

    /**
     * 任务ID,底层处理完成后返回
     */
    @TableField("task_id")
    private String taskId;

    /**
     * 任务处理结果描述
     */
    @TableField("run_msg")
    private String runMsg;

    /**
     * 工作流节点需要的模型id
     */
    @TableField("model_id")
    private String modelId;

    /**
     * 工作流节点需要的psi的id
     */
    @TableField("psi_id")
    private String psiId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    private static final long serialVersionUID = 1L;


}
