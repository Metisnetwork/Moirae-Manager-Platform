package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工作流任务资源表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_task_resource")
public class WorkflowTaskResource implements Serializable {


    /**
     * 工作流任务配置id
     */
    @TableId("workflow_task_id")
    private Long workflowTaskId;

    /**
     * 所需的内存 (单位: byte)
     */
    @TableField("cost_mem")
    private Long costMem;

    /**
     * 所需的核数 (单位: 个)
     */
    @TableField("cost_cpu")
    private Integer costCpu;

    /**
     * GPU核数(单位：核)
     */
    @TableField("cost_gpu")
    private Integer costGpu;

    /**
     * 所需的带宽 (单位: bps)
     */
    @TableField("cost_bandwidth")
    private Long costBandwidth;

    /**
     * 所需的运行时长 (单位: ms)
     */
    @TableField("run_time")
    private Long runTime;

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
