package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 工作流专家模式节点表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_setting_expert")
public class WorkflowSettingExpert implements Serializable {


    /**
     * 工作流节点表ID(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工作流id
     */
    @TableField("workflow_id")
    private Long workflowId;

    /**
     * 工作流版本号
     */
    @TableField("workflow_version")
    private Integer workflowVersion;

    /**
     * 工作流中节点的顺序,从1开始
     */
    @TableField("node_step")
    private Integer nodeStep;

    /**
     * 节点名称
     */
    @TableField("node_name")
    private String nodeName;

    /**
     * 工作流任务配置id
     */
    @TableField("psi_task_step")
    private Integer psiTaskStep;

    /**
     * 工作流任务配置id
     */
    @TableField("task_step")
    private Integer taskStep;

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
