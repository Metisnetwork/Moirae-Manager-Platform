package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.moirae.rosettaflow.mapper.enums.WorkflowCreateModeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 工作流表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow")
public class Workflow implements Serializable {


    /**
     * 工作流ID(自增长)
     */
    @TableId(value = "workflow_id", type = IdType.AUTO)
    private Long workflowId;

    /**
     * 创建模式:1-专家模式,2-向导模式
     */
    @TableField("create_mode")
    private WorkflowCreateModeEnum createMode;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 工作流名称
     */
    @TableField("workflow_name")
    private String workflowName;

    /**
     * 工作流描述
     */
    @TableField("workflow_desc")
    private String workflowDesc;

    /**
     * 算法id
     */
    @TableField("algorithm_id")
    private Long algorithmId;

    /**
     * 算法名称
     */
    @TableField("algorithm_name")
    private String algorithmName;

    /**
     * 计算流程id
     */
    @TableField("calculation_process_id")
    private Long calculationProcessId;

    /**
     * 计算流程名称
     */
    @TableField("calculation_process_name")
    private String calculationProcessName;

    /**
     * 最后运行时间
     */
    @TableField("last_run_time")
    private LocalDateTime lastRunTime;

    /**
     * 是否设置完成:  0-否  1-是
     */
    @TableField("is_setting_completed")
    private Integer isSettingCompleted;

    /**
     * 向导模式下当前步骤
     */
    @TableField("calculation_process_step")
    private Integer calculationProcessStep;

    /**
     * 是否删除: 0-否  1-是
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 当前最大版本号,从1开始
     */
    @TableField("workflow_version")
    private Long workflowVersion;

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
